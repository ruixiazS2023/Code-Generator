package com.richard.maker.meta.enums;

public enum ModelTypeEnum {
    STRING("String","String"),
    BOOLEAN("Boolean","Boolean");

    private final String text;
    private final String value;

    ModelTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() { return text; }

    public String getValue() { return value; }
}
