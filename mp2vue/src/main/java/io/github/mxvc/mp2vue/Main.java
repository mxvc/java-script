package io.github.mxvc.mp2vue;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static final String TARGET = "D:\\ws\\yunying-sichuan-app\\pages\\points\\mall\\mall.vue";


    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.parse(ResourceUtil.readUtf8Str("source.html"), Parser.xmlParser());

        Element body = doc.selectFirst("body");

        Elements elements = body.select("*");
        for (Element element : elements) {
            String name = element.tagName();
            System.out.println(name);
            if (name.startsWith("wx-mp-html")) {
                element.remove();
                continue;
            }
            if (name.equals("span")) {
                String text = element.text();
                if (StrUtil.isBlank(text)) {
                    element.remove();
                    continue;
                }
            }

            if (name.startsWith("wx-")) {
                element.tagName(name.substring(3));
            }

            if (name.equals("wx-image")) {
                element.empty();
            }
            if (name.equals("wx-text")) {
                element.html(element.text());
            }

            if(element.hasAttr("is")){ // is 有些@符号会报错
                element.removeAttr("is");
            }
        }




        // 处理css

        Elements styles = doc.select("style[wxss:path]");
        List<Element> styleList = styles.stream().filter(s -> {
            String path = s.attr("wxss:path");
            if (path.contains("@")) {
                return false;
            }
            if(StrUtil.containsAny(path, "unknown","./app.wxss", "npm", "tab-bar")){
                return false;
            }
            return true;
        }).collect(Collectors.toList());

        Assert.state(styleList.size() == 1, "没有找到真正的样式");


        Element style = styleList.get(0);


        replaceTarget(body.firstElementChild(), style);

    }


    private static void replaceTarget(Element template, Element style) {
        String vue = FileUtil.readUtf8String(TARGET);

        vue = "<root>" + vue + "</root>";

        Document root = Jsoup.parse(vue, Parser.xmlParser());

        Element t = root.selectFirst("template");
        t.empty();
        t.appendChild(template);

        root.select("style").remove();
        root.appendChild(style);


        String result = root.html();

        result = result.replace("<root>", "").replace("</root>", "");


        System.out.println(result);
        FileUtil.writeUtf8String(result, TARGET);
    }

}
