package com.richard.maker.cli.command;


import cn.hutool.core.util.ReflectUtil;
import com.richard.maker.model.DataModel;
import picocli.CommandLine.Command;

import java.lang.reflect.Field;

@Command(name = "config", description = "Config command",mixinStandardHelpOptions = true)
public class ConfigCommand implements Runnable {
    public void run() {
        Field[] fields = ReflectUtil.getFields(DataModel.class);
        for(Field field : fields){
            System.out.println("Field name: " + field.getName());
            System.out.println("Field type: " + field.getType());
            System.out.println("----------");
        }
    }
}
