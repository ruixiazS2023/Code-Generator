package com.richard.maker.generator;
import cn.hutool.core.io.FileUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public class ScriptGenerator {

    public static void doGenerator(String outputPath,String jarPath) {
        StringBuilder sb = new StringBuilder();

//        Windows script


//        Linux script
//        #!/bin/bash
//        java -jar target/code-generator-maker-1.0-SNAPSHOT.jar "$@"
        sb.append("#!/bin/bash\n");
        sb.append(String.format("java -jar %s \"$@\"", jarPath)).append("\n");

        FileUtil.writeBytes(sb.toString().getBytes(StandardCharsets.UTF_8), outputPath);
        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
        try {
            Files.setPosixFilePermissions(Paths.get(outputPath), permissions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
