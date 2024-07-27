package com.richard.maker.meta;


import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

public class MetaManager {

    private static volatile Meta meta;

    public static Meta getMetaObject() {
        if (meta == null) {
            synchronized (MetaManager.class) {
                if (meta == null) {
                    meta = initMeta();
                }
            }
        }
        return meta;
    }

    private static Meta initMeta() {
        String jsonString = ResourceUtil.readUtf8Str("meta.json");
        Meta newMeta = JSONUtil.toBean(jsonString, Meta.class);

        // Validate and fill the metaObject
        MetaValidator.doValidateAndFill(newMeta);
        return newMeta;
    }
    public static void main(String[] args) {
        String jsonString = ResourceUtil.readUtf8Str("meta.json");
        Meta newMeta = JSONUtil.toBean(jsonString, Meta.class);

        Meta meta = getMetaObject();

        System.out.println(meta);
    }
}
