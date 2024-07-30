package com.richard.maker.template;

import cn.hutool.core.util.StrUtil;
import com.richard.maker.meta.Meta;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TemplateMakerUtils {
    public static List<Meta.FileConfig.FileInfo> removeFroupFilesFromRoot(List<Meta.FileConfig.FileInfo> fileInfoList) {

        // get all group
        List<Meta.FileConfig.FileInfo> groupFileInfoList = fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(Collectors.toList());

        // get all fiels
        List<Meta.FileConfig.FileInfo> groupInnerFileInfoList = groupFileInfoList.stream()
                .flatMap(fileInfo -> fileInfo.getFiles().stream())
                .collect(Collectors.toList());

        //get all input path
        Set<String> fileInputPathSet = groupInnerFileInfoList.stream()
                .map(Meta.FileConfig.FileInfo::getInputPath)
                .collect(Collectors.toSet());

        // remove all files whose name is in set
        return fileInfoList.stream()
                .filter(fileInfo -> !fileInputPathSet.contains(fileInfo.getInputPath()))
                .collect(Collectors.toList());
    }
}
