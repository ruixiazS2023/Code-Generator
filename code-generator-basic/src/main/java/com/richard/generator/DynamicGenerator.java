package com.richard.generator;

import com.richard.model.MainTemplateConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;

public class DynamicGenerator {
    public static void main(String[] args) throws IOException, TemplateException {
        String projectPath = System.getProperty("user.dir");
        String inputPath = projectPath + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String outputPath = projectPath + File.separator + "MainTemplate.java";
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setLoop(true);
        mainTemplateConfig.setAuthor("Richard");
        mainTemplateConfig.setOutputText("Sum: ");
        doGenerator(inputPath, outputPath, mainTemplateConfig);
    }

    public static void doGenerator(String inputPath, String outputPath, Object model) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

        // set template directory
        File templateDir = new File(inputPath).getParentFile();
        configuration.setDirectoryForTemplateLoading(templateDir);
        configuration.setDefaultEncoding("UTF-8");

        // create template object, loading targeted template file
        String templateName = new File(inputPath).getName();
        Template template = configuration.getTemplate(templateName);

        //generate
        Writer out = new FileWriter(outputPath);
        template.process(model, out);
        out.close();
    }
}
