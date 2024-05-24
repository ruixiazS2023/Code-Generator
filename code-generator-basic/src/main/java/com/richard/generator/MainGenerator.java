package com.richard.generator;

import com.richard.model.MainTemplateConfig;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class MainGenerator {
    public static void main(String[] args) throws IOException, TemplateException {
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setLoop(true);
        mainTemplateConfig.setAuthor("Richard");
        mainTemplateConfig.setOutputText("Sum: ");
        doGenerator(mainTemplateConfig);
    }

    public static void doGenerator(Object model) throws IOException, TemplateException {
        String projectPath = System.getProperty("user.dir");
        File parentFile = new File(projectPath).getParentFile();
        String inputPath = new File(parentFile,"code-generator-demo-projects/acm-template").getAbsolutePath();
        String outputPath = projectPath;

        //generate static files
        StaticGenerator.copyFilesByHutool(inputPath, outputPath);

        //generate dynamic files
        String inputDynamicFilePath = projectPath + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String outputDynamicFilePath = projectPath + File.separator + "acm-template/src/com/richard/acm/MainTemplate.java";
        DynamicGenerator.doGenerator(inputDynamicFilePath, outputDynamicFilePath, model);

    }
}
