package com.richard.maker.meta;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.richard.maker.meta.enums.FileGenerateTypeEnum;
import com.richard.maker.meta.enums.FileTypeEnum;
import com.richard.maker.meta.enums.ModelTypeEnum;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class MetaValidator {
    public static void doValidateAndFill(Meta meta) {
        basicInfoCheck(meta);
        FileConfigCheck(meta);
        modelInfoCheck(meta);
    }

    private static void modelInfoCheck(Meta meta) {
        Meta.ModelConfig modelConfig = meta.getModelConfig();

        if (modelConfig == null) {
            return;
        }
        List<Meta.ModelConfig.ModelInfo> models = modelConfig.getModels();
        if (CollectionUtil.isNotEmpty(models)) {
            for (Meta.ModelConfig.ModelInfo modelInfo : models) {
                String groupKey = modelInfo.getGroupKey();

                if (StrUtil.isNotEmpty(groupKey)) {
                    List<Meta.ModelConfig.ModelInfo> subModelInfoList = modelInfo.getModels();
                    String allArgsStr = subModelInfoList.stream()
                            .map(subModelInfo -> String.format("\"--%s\"", subModelInfo.getFieldName()))
                            .collect(Collectors.joining(", "));
                    modelInfo.setAllArgsStr(allArgsStr);
                    continue;
                }

                String fieldName = modelInfo.getFieldName();
                if (StrUtil.isBlank(fieldName)) {
                    throw new MetaException("please enter fieldName");
                }

                String modelInfoType = modelInfo.getType();
                if (StrUtil.isBlank(modelInfoType)) {
                    modelInfo.setType(ModelTypeEnum.STRING.getValue());
                }
            }
        }
    }

    private static void FileConfigCheck(Meta meta) {
        Meta.FileConfig fileConfig = meta.getFileConfig();

        if (fileConfig == null) {
            return;
        }
        String sourceRootPath = fileConfig.getSourceRootPath();
        if (StrUtil.isBlank(sourceRootPath)) {
            throw new MetaException("please enter sourceRootPath");
        }

        String inputRootPath = fileConfig.getInputRootPath();
        if (StrUtil.isBlank(inputRootPath)) {
            String defaultInputRootPath = ".source" + File.separator + FileUtil.getLastPathEle(Paths.get(sourceRootPath)).getFileName().toString();
            fileConfig.setInputRootPath(defaultInputRootPath);
        }

        String outputRootPath = fileConfig.getOutputRootPath();
        if (StrUtil.isBlank(outputRootPath)) {
            String defaultOutputRootPath = "generated";
            fileConfig.setOutputRootPath(defaultOutputRootPath);
        }

        String fileConfigType = fileConfig.getType();
        if (StrUtil.isBlank(fileConfigType)) {
            String defaultFileConfigType = FileTypeEnum.DIR.getValue();
            fileConfig.setType(defaultFileConfigType);
        }

        List<Meta.FileConfig.FileInfo> files = fileConfig.getFiles();
        if (CollectionUtil.isNotEmpty(files)) {
            for (Meta.FileConfig.FileInfo fileInfo : files) {
                String type = fileInfo.getType();

                if(FileTypeEnum.GROUP.getValue().equals(type)){
                    continue;
                }

                String inputPath = fileInfo.getInputPath();
                if (StrUtil.isBlank(inputPath)) {
                    throw new MetaException("please enter path");
                }

                String outputPath = fileInfo.getOutputPath();
                if (StrUtil.isEmpty(outputPath)) {
                    fileInfo.setOutputPath(inputPath);
                }

                if (StrUtil.isBlank(type)) {
                    if (StrUtil.isBlank(FileUtil.getSuffix(inputPath))) {
                        fileInfo.setType(FileTypeEnum.DIR.getValue());
                    } else {
                        fileInfo.setType(FileTypeEnum.FILE.getValue());
                    }
                }

                String generateType = fileInfo.getGenerateType();

                if (StrUtil.isBlank(generateType)) {
                    if (inputPath.endsWith(".ftl")) {
                        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
                    } else {
                        fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
                    }
                }
            }
        }
    }

    private static void basicInfoCheck(Meta meta) {
        String name = StrUtil.blankToDefault(meta.getName(), "my-generator");
        String description = StrUtil.blankToDefault(meta.getDescription(), "my-generator");
        String basePackage = StrUtil.blankToDefault(meta.getBasePackage(), "com.richard");
        String author = StrUtil.blankToDefault(meta.getAuthor(), "richard");
        String version = StrUtil.blankToDefault(meta.getVersion(), "1.0");
        String createTime = StrUtil.blankToDefault(meta.getCreateTime(), DateUtil.now());

        meta.setName(name);
        meta.setDescription(description);
        meta.setBasePackage(basePackage);
        meta.setAuthor(author);
        meta.setVersion(version);
        meta.setCreateTime(createTime);
    }
}
