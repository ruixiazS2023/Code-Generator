package com.richard.maker.template.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

@Getter
public enum FileFilterRangeEnum {

    FILE_NAME("fileName","fileName"),
    FILE_CONTENT("fileContent","fileContent");

    private final String text;
    private final String value;


    FileFilterRangeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static FileFilterRangeEnum getEnumByValue(String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (FileFilterRangeEnum fileFilterRangeEnum : FileFilterRangeEnum.values()) {
            if (fileFilterRangeEnum.value.equals(value)) {
                return fileFilterRangeEnum;
            }
        }
        return null;
    }
}
