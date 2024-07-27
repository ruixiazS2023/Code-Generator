package ${basePackage}.generator;
import cn.hutool.core.io.FileUtil;
public class StaticGenerator {
    public static void copyFilesByHutool(String sourcePath, String targetPath) {
        FileUtil.copy(sourcePath, targetPath,false);
    }
}