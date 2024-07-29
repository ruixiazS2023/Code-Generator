package com.richard.maker.template.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileFilterConfig {

    /**
     * filter range
     */
    private String range;

    /**
     * filter rule
     */
    private String rule;

    /**
     * filter value
     */
    private String value;
}
