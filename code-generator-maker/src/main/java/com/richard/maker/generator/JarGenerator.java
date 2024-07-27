package com.richard.maker.generator;

import java.io.*;

public class JarGenerator {
    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        String MvnCmd = "mvn clean package -DskipTests=true";
        ProcessBuilder processBuilder = new ProcessBuilder(MvnCmd.split(" "));
        processBuilder.directory(new File(projectDir));
        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.waitFor();
        System.out.println("JarGenerator.doGenerate exitCode: " + exitCode);
    }
}
