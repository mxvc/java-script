package qqmusic;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;

/**
 * 将ogg文件转换为mp3并拷贝都指定目录
 */
public class OggToMp3 {

    public static final String SOURCE_DIR = "C:\\Users\\zt\\Music\\QQMusic";
    public static final String TARGET_DIR = "E:\\";
    public static void main(String[] args) {
        File[] files = new File(SOURCE_DIR).listFiles();
        Assert.notNull(files,"没有文件");


        for (int i = 0; i < files.length; i++) {
            System.out.println();
            System.out.printf("第%s个，共%s个。 \n", i+1, files.length) ;
            File file = files[i];
            System.out.println("处理文件" + file);
            if (file.getName().endsWith(".lrc")) {
                copy(file);
                continue;
            }
            if (file.getName().endsWith(".mp3")) {
                copy(file);
                continue;
            }
            if (file.getName().endsWith(".ogg")) {
                File newFile = convert(file);
                copy(newFile);
            }
        }


    }

    private static File convert(File file){
        // 设置FFmpeg的路径
        String ffmpegPath = "ffmpeg";

        // 要转换的视频文件路径
        String inputVideoPath = file.getAbsolutePath();

        // 转换后的视频文件路径
        File outputVideoPath = new File(TARGET_DIR ,  FileUtil.mainName(file.getName())  + ".mp3");

        // 构建FFmpeg命令
        String command = ffmpegPath + " -i \"" + inputVideoPath + "\" \"" + outputVideoPath.getAbsolutePath() + "\"";

        System.out.println(command);
        // 执行FFmpeg命令
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        try {
            Process process = processBuilder.start();
            // 等待FFmpeg命令执行完成
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("视频转换成功");
            } else {
                System.out.println("视频转换失败");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return outputVideoPath;
    }

    private static void copy(File file){
        FileUtil.copy(file, new File(TARGET_DIR), true);
    }
}
