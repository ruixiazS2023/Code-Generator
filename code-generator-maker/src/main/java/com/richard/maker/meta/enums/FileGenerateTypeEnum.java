package com.richard.maker.meta.enums;

public enum FileGenerateTypeEnum {
    DYNAMIC("dynamic","dynamic"),

    STATIC("static","static");

    private final String text;

    private final String value;


    FileGenerateTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() { return text; }

    public String getValue() { return value; }
}
