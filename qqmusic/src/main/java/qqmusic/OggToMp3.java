package qqmusic;

import java.io.File;

/**
 * 将ogg文件转换为mp3并拷贝都指定目录
 */
public class OggToMp3 {

    public static final String SOURCE_DIR = "C:\\Users\\zt\\Music\\VipSongsDownload";
    public static final String TARGET_DIR = "E:\\";
    public static void main(String[] args) {
        File[] files = new File(SOURCE_DIR).listFiles();

        for (File file : files) {
            if(file.getName().endsWith(".lrc")){
                copy(file);
                continue;
            }
            if(file.getName().endsWith(".mp3")){
                copy(file);
                continue;
            }
            if(file.getName().endsWith(".ogg")){
              File newFile =   convert(file);
                copy(file);
            }
        }


    }

    private static File convert(File file){

        return file;
    }

    private static void copy(File file){

    }
}
