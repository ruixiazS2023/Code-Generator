package ${basePackage}.cli.command;

import ${basePackage}.model.DataModel;
import lombok.Data;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import java.util.concurrent.Callable;
import ${basePackage}.generator.file.FileGenerator;

@Command(name = "generate", description = "Generate command",mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable<Integer> {

    <#list modelConfig.models as model>
    @Option(names = {<#if model.abbr??>"-${model.abbr}", </#if>"--${model.fieldName}" }, arity="0..1", <#if model.description??>description = "${model.description}", </#if>interactive = true, echo = true)
    private ${model.type} ${model.fieldName}<#if model.defaultValue??> = ${model.defaultValue?c}</#if>;
    </#list>
    public Integer call() throws Exception {
        DataModel mainTemplateConfig = new DataModel();
        mainTemplateConfig.setLoop(loop);
        mainTemplateConfig.setAuthor(author);
        mainTemplateConfig.setOutputText(outputText);
        FileGenerator.doGenerator(mainTemplateConfig);
        return 0;
    }
}
