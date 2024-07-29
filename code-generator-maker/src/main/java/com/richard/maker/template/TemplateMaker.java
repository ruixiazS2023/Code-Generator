package com.richard.maker.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.richard.maker.meta.Meta;
import com.richard.maker.meta.enums.FileGenerateTypeEnum;
import com.richard.maker.meta.enums.FileTypeEnum;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TemplateMaker {
    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> filesList){
        List<Meta.FileConfig.FileInfo> files = new ArrayList<>(
                filesList.stream().collect(
                        Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r))
                        .values());
        return files;
    }
    private static List<Meta.ModelConfig.ModelInfo> distinctModels(List<Meta.ModelConfig.ModelInfo> modelsList){
        List<Meta.ModelConfig.ModelInfo> newModelList = new ArrayList<>(
                modelsList.stream().collect(
                                Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r))
                        .values());
        return newModelList;
    }
    private static long makeTemplate(Meta newMeta, String originProjectPath, List<String> inputFilePathList, Meta.ModelConfig.ModelInfo modelInfo, String searchStr, Long id){
        if (id == null){
            id = IdUtil.getSnowflakeNextId();
        }
        //copy directory
        String projectPath = System.getProperty("user.dir");
        String templateDirPath = projectPath + File.separator + ".temp";
        String templatePath = templateDirPath + File.separator + id;
        if (!FileUtil.exist(templatePath)) {
            FileUtil.mkdir(templatePath);
            FileUtil.copy(originProjectPath, templatePath, true);
        }
        String sourceRootPath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();

        List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>();
        for (String inputFilePath : inputFilePathList){
            String inputFileAbsolutePath = sourceRootPath + File.separator + inputFilePath;
            if (FileUtil.isDirectory(inputFileAbsolutePath)){
                List<File> files = FileUtil.loopFiles(inputFileAbsolutePath);
                for (File file : files){
                    Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(modelInfo, searchStr, sourceRootPath, file);
                    newFileInfoList.add(fileInfo);
                }
            } else {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(modelInfo, searchStr, sourceRootPath, new File(inputFileAbsolutePath));
                newFileInfoList.add(fileInfo);
            }
        }

        //generate meta json
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";
        if (FileUtil.exist(metaOutputPath)){
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            BeanUtil.copyProperties(newMeta, oldMeta, CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;

            // add meta info
            List<Meta.FileConfig.FileInfo> files = newMeta.getFileConfig().getFiles();
            files.addAll(newFileInfoList);
            List<Meta.ModelConfig.ModelInfo> modelInfos = newMeta.getModelConfig().getModels();
            modelInfos.add(modelInfo);

            // meta distinct
            newMeta.getFileConfig().setFiles(distinctFiles(files));
            newMeta.getModelConfig().setModels(distinctModels(modelInfos));
        } else {
            // create meta parameters
            Meta.FileConfig fileConfig = new Meta.FileConfig();
            newMeta.setFileConfig(fileConfig);
            fileConfig.setSourceRootPath(sourceRootPath);
            List<Meta.FileConfig.FileInfo> files = new ArrayList<>();
            fileConfig.setFiles(files);
            files.addAll(newFileInfoList);

            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            newMeta.setModelConfig(modelConfig);
            List<Meta.ModelConfig.ModelInfo> modelInfos = new ArrayList<>();
            modelConfig.setModels(modelInfos);
            modelInfos.add(modelInfo);
        }

        //output meta json
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);

        return id;
    }

    private static Meta.FileConfig.FileInfo makeFileTemplate(Meta.ModelConfig.ModelInfo modelInfo, String searchStr,String sourceRootPath,File inputFile){
        String fileInputAbsolutePath = inputFile.getAbsolutePath();
        String fileOutputAbsolutePath = fileInputAbsolutePath + ".ftl";

        String fileInputPath = fileInputAbsolutePath.replace(sourceRootPath + "/", "");
        String fileOutputPath = fileInputPath + ".ftl";
        String fileContent;
        if(FileUtil.exist(fileInputAbsolutePath)){
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        }
        else{
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }

        // replace the Sum string by ${}
        String replacement = String.format("${%s}",modelInfo.getFieldName());
        String newFileContent = StrUtil.replace(fileContent,searchStr, replacement);

        //set file info
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());

        if(newFileContent.equals(fileContent)){
            fileInfo.setOutputPath(fileInputPath);
            fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
        }
        else{
            fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
            //output template file
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        }
        return fileInfo;
    }
    public static void main(String[] args) {
        //set origin file path
        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent() + File.separator + "code-generator-demo-projects/acm-template";
        String inputFilePath1 = "src/com/richard/acm/MainTemplate.java";
        String inputFilePath2 = "src/com/richard/acm/MainTemplate2.java";
        List<String> inputFilePathList = Arrays.asList(inputFilePath1, inputFilePath2);

        // copy directory
        String name = "acm-template-generator";
        String description = "ACM template generator example";
        Meta meta = new Meta();
        meta.setName(name);
        meta.setDescription(description);

        // set model configuration
//        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
//        modelInfo.setFieldName("outputText");
//        modelInfo.setType("String");
//        modelInfo.setDefaultValue("sum = ");
//        String searchStr = "Sum: ";
//        long id = makeTemplate(meta, originProjectPath, inputFilePath, modelInfo, searchStr, null);
//        System.out.println("id = " + id);

        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("className");
        modelInfo.setType("String");
        String searchStr = "MainTemplate";
        long id = makeTemplate(meta, originProjectPath, inputFilePathList, modelInfo, searchStr, 1817865184677670912L);
        System.out.println("id = " + id);
    }
}
