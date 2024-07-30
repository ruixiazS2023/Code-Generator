package com.richard.maker.template.model;

import com.richard.maker.meta.Meta;
import lombok.Data;

@Data
public class TemplateMakerConfig {
    private Long id;
    private Meta meta = new Meta();
    private String originProjectPath;
    TemplateMakerModelConfig modelConfig = new TemplateMakerModelConfig();
    TemplateMakerFileConfig fileConfig = new TemplateMakerFileConfig();
    TemplateMakerOutputConfig outputConfig = new TemplateMakerOutputConfig();
}
