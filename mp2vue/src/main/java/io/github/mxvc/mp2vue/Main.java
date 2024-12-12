package io.github.mxvc.mp2vue;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;

public class Main {

    public static final String TARGET = "D:\\ws\\yunying-sichuan-app\\pages\\points\\mall\\mall.vue";



    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.parse(ResourceUtil.readUtf8Str("source.html"));

        Element view = doc.selectFirst("body");


        System.out.println(view);

        replaceTargetTemplate(view.html());

    }


    private static void replaceTargetTemplate(String content){
        String vue = FileUtil.readUtf8String(TARGET);

        vue = "<html>" + vue + "</html>";

        Document doc = Jsoup.parse(vue);

        Element t = doc.selectFirst("template");
        t.html(content);


        FileUtil.writeUtf8String(TARGET, t.html());
    }

}
