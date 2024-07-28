package com.richard.maker.meta.enums;

public enum FileTypeEnum {
    DIR("dir","dir"),
    FILE("file","file"),
    GROUP("group","group");

    private final String text;
    private final String value;


    FileTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() { return text; }

    public String getValue() { return value; }
}
