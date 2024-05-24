package com.richard.cli.command;

import com.richard.model.MainTemplateConfig;
import freemarker.template.TemplateException;
import lombok.Data;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.richard.generator.MainGenerator;

@Command(name = "generate", description = "Generate command",mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable<Integer> {

    @Option(names = { "-l", "--loop" }, arity="0..1", description = "whether loop", interactive = true, echo = true)
    private boolean loop;

    @Option(names = {"-a","--author"}, arity="0..1", description = "author name", interactive = true, echo = true)
    private String author = "Richard";

    @Option(names = {"-o","--outputText"}, arity="0..1", description = "outputText", interactive = true, echo = true)
    private String outputText = "Sum = ";
    public Integer call() throws Exception {
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
//        mainTemplateConfig.setLoop(loop);
//        mainTemplateConfig.setAuthor(author);
//        mainTemplateConfig.setOutputText(outputText);
        MainGenerator.doGenerator(mainTemplateConfig);
        return 0;
    }

}
