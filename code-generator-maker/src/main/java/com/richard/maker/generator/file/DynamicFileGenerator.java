package com.richard.maker.generator;

import cn.hutool.core.io.FileUtil;
import com.richard.maker.model.DataModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;

public class DynamicFileGenerator {

    public static void doGenerator(String inputPath, String outputPath, Object model) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

        // set template directory
        File templateDir = new File(inputPath).getParentFile();
        configuration.setDirectoryForTemplateLoading(templateDir);
        configuration.setDefaultEncoding("UTF-8");

        // create template object, loading targeted template file
        String templateName = new File(inputPath).getName();
        Template template = configuration.getTemplate(templateName);

        if(!FileUtil.exist(outputPath)){
            FileUtil.touch(outputPath);
        }
        //generate
        Writer out = new FileWriter(outputPath);
        template.process(model, out);
        out.close();
    }
}
