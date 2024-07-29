package com.richard.maker.template.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

@Getter
public enum FileFilterRuleEnum {

    CONTAINS("contains","contains"),
    STARTS_WITH("startsWith","startsWith"),
    ENDS_WITH("endsWith","endsWith"),
    REGEX("regex","regex"),
    EQUALS("equals","equals");

    private final String text;

    private final String value;

    FileFilterRuleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static FileFilterRuleEnum getEnumByValue(String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (FileFilterRuleEnum rule : FileFilterRuleEnum.values()) {
            if(rule.value.equals(value)) {
                return rule;
            }
        }
        return null;
    }
}
