package com.richard.maker.generator.file;
import cn.hutool.core.io.FileUtil;
public class StaticFileGenerator {
    public static void copyFilesByHutool(String sourcePath, String targetPath) {
        FileUtil.copy(sourcePath, targetPath,false);
    }
}
