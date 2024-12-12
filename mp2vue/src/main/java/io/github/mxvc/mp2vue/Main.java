package io.github.mxvc.mp2vue;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;

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


        replaceTargetTemplate(body.firstElementChild());

    }


    private static void replaceTargetTemplate(Element view) {
        String vue = FileUtil.readUtf8String(TARGET);

        vue = "<root>" + vue + "</root>";

        Document root = Jsoup.parse(vue, Parser.xmlParser());

        Element t = root.selectFirst("template");
        t.empty();
        t.appendChild(view);


        String result = root.html();

        result = result.replace("<root>", "").replace("</root>", "");


        System.out.println(result);
        FileUtil.writeUtf8String(result, TARGET);
    }

}
