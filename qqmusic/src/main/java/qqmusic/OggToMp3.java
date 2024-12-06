package qqmusic;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 将ogg文件转换为mp3并拷贝都指定目录
 */
@Slf4j
public class OggToMp3 {

    public static final boolean DELETE_SOURCE_FILE = true;
    public static final String SOURCE_DIR = "C:\\Users\\zt\\Music\\QQMusic";
    public static final String TARGET_DIR = "E:\\";

    public static void main(String[] args) throws InterruptedException {
        File[] files = new File(SOURCE_DIR).listFiles();
        Assert.notNull(files,"没有文件");


        int length = files.length;

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < length; i++) {

            int index = i;
            File file = files[i];
            executorService.submit(() -> process(index, length, file));
        }
        // 等待结束
        executorService.awaitTermination(1, TimeUnit.DAYS);
    }

    private static void process(int i, int length, File file) {
        log.info("----------------");
        log.info("{}/{}",   i + 1, length); ;
        log.info("处理文件 {}" , file);

        File targetFile = new File(TARGET_DIR, file.getName());
        if(targetFile.exists()){
            log.warn("已存在，忽略");
            delete(file);

            return;
        }

        if (file.getName().endsWith(".lrc")) {
            move(file);
            return;
        }
        if (file.getName().endsWith(".mp3")) {
            move(file);
            return;
        }
        if (file.getName().endsWith(".ogg")) {
            File targetMp3 = new File(TARGET_DIR, FileUtil.mainName(file) + ".mp3");
            if(FileUtil.exist(targetMp3)){
                log.warn("已存在mp3，忽略");
                delete(file);
                return;
            }

            File newFile = convert(file);
            move(newFile);
            delete(file);
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


        String str = RuntimeUtil.execForStr(command);

        System.out.println(str);


        return outputVideoPath;
    }

    private static void move(File file){
        FileUtil.move(file, new File(TARGET_DIR), true);
    }
    private static void delete(File file){
        if(DELETE_SOURCE_FILE){
            log.info("删除文件 {}",file);
            FileUtil.del(file);
        }
    }
}
