package com.richard.model;

import lombok.Data;

@Data
public class MainTemplateConfig {
    private boolean loop;
    private String author = "Richard";
    private String outputText = "Sum: ";
}
