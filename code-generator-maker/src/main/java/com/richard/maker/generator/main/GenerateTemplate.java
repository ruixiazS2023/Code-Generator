package com.richard.maker.generator.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.richard.maker.generator.JarGenerator;
import com.richard.maker.generator.ScriptGenerator;
import com.richard.maker.generator.file.DynamicFileGenerator;
import com.richard.maker.meta.Meta;
import com.richard.maker.meta.MetaManager;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class GenerateTemplate {

    public void doGenerate() throws TemplateException, IOException,InterruptedException {
        Meta meta = MetaManager.getMetaObject();

        String projectPath = System.getProperty("user.dir");
        String outputPath = projectPath + File.separator + "generated" + File.separator + meta.getName();
        if (!FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }
        
        // 1. copy source file
        String sourceCopyTargetPath = copySource(meta, outputPath);

        // 2. generate code
        generateCode(meta, outputPath);

        // 3. build jar
        String jarPath = buildJar(meta, outputPath);
        
        // 4. ensuplate shell script
        String shellScriptPath = buildScript(outputPath, jarPath);


        // 5. build dist
        buildDist(outputPath, jarPath, shellScriptPath, sourceCopyTargetPath);
        
    }

    protected void generateCode(Meta meta, String outputPath) throws IOException, TemplateException {
        // read resources list
        ClassPathResource resource = new ClassPathResource("");
        String inputRescourcePath = resource.getAbsolutePath();

        // java base Package Path
        String outputBasePackage = meta.getBasePackage();
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(outputBasePackage, "."));
        String outputBaseJavaPackagePath = outputPath + File.separator + "src/main/java/" + outputBasePackagePath;

        String inputFilePath;
        String outputFilePath;

        // generate model.DataModel
        inputFilePath = inputRescourcePath + File.separator + "templates/java/model/DataModel.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "model" + File.separator + "DataModel.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        // generate Cli.command.ConfigCommand
        inputFilePath = inputRescourcePath + File.separator + "templates/java/cli/command/ConfigCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/command/ConfigCommand.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);


        // generate Cli.command.GenerateCommand
        inputFilePath = inputRescourcePath + File.separator + "templates/java/cli/command/GenerateCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/command/GenerateCommand.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        // generate Cli.command.ListCommand
        inputFilePath = inputRescourcePath + File.separator + "templates/java/cli/command/ListCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/command/ListCommand.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        // generate cli.CommandExecutor
        inputFilePath = inputRescourcePath + File.separator + "templates/java/cli/CommandExecutor.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/CommandExecutor.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        // Main
        inputFilePath = inputRescourcePath + File.separator + "templates/java/Main.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "Main.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        // generator.MainGenerator
        inputFilePath = inputRescourcePath + File.separator + "templates/java/generator/MainGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "generator/MainGenerator.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);


        // generator.DynamicGenerator
        inputFilePath = inputRescourcePath + File.separator + "templates/java/generator/DynamicGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "generator/DynamicGenerator.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);


        // generator.StaticGenerator
        inputFilePath = inputRescourcePath + File.separator + "templates/java/generator/StaticGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "generator/StaticGenerator.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //generate pom.xml
        inputFilePath = inputRescourcePath + File.separator + "templates/pom.xml.ftl";
        outputFilePath = outputPath + File.separator + "pom.xml";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //generate read.me
        inputFilePath = inputRescourcePath + File.separator + "templates/README.md.ftl";
        outputFilePath = outputPath + File.separator + "README.md";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);
    }

    protected void buildDist(String outputPath, String jarPath, String shellScriptPath, String sourceCopyTargetPath) {
        // simple generated
        String distOutputPath = outputPath + "-dist";
        // copy jar to dist
        String targetAbsolutePath = distOutputPath + File.separator + "target";
        FileUtil.mkdir(targetAbsolutePath);
        String jarAbsolutePath = outputPath + File.separator + jarPath;
        FileUtil.copy(jarAbsolutePath, distOutputPath, true);
        //copy shell script to dist
        FileUtil.copy(shellScriptPath, distOutputPath, true);
        //copy .source
        FileUtil.copy(sourceCopyTargetPath, distOutputPath, true);
    }

    protected String copySource(Meta meta, String outputPath) {
        String sourceRootPath = meta.getFileConfig().getSourceRootPath();
        String sourceCopyTargetPath = outputPath + File.separator + ".source";
        FileUtil.copy(sourceRootPath, sourceCopyTargetPath, true);
        return sourceCopyTargetPath;
    }

    protected String buildJar(Meta meta, String outputPath) throws IOException, InterruptedException{
        JarGenerator.doGenerate(outputPath);
        String jarName = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        String jarPath = "target/" + jarName;
        return jarPath;
    }

    /**
    * ensuplate shell script
    * @param outputPath
    * @param jarPath
    * @return shellScriptPath
     */
    protected String buildScript(String outputPath,String jarPath) {
        String shellScriptPath = outputPath + File.separator + "generator";
        ScriptGenerator.doGenerator(shellScriptPath, jarPath);
        return shellScriptPath;
    }




}
