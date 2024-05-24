package com.richard;

import java.io.File;

import static com.richard.generator.StaticGenerator.copyFilesByHutool;

public class Main {
    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        File parentFile = new File(projectPath).getParentFile();
        String inputPath = new File(parentFile,"code-generator-demo-projects/acm-template").getAbsolutePath();
        String outputPath = projectPath;
        copyFilesByHutool(inputPath, outputPath);
    }
}