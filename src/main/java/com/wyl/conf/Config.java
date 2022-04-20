package com.wyl.conf;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2022/4/19
 * 描述    : 配置对象，内存中只需要存在一份实例，所以设计为单例
 */
public class Config {
    private static class Holder {
        private static Config config = new Config();
    }

    private Config() {
        properties = new HashMap<>();
    }

    public static Config getInstance() {
        return Holder.config;
    }

    private HashMap<String, Object> properties;

    /**
     * 开始解析配置问价
     *
     * @param confPath
     * @throws FileNotFoundException
     */
    public void init(String confPath) throws FileNotFoundException {
        if (confPath == null || confPath.isEmpty()) throw new IllegalArgumentException("配置文件的路劲不能为空");
        // 开始解析配置文件
        Yaml yaml = new Yaml();
        properties = (HashMap<String, Object>) yaml.loadAs(new FileInputStream(confPath), Map.class);
    }

    /**
     * 据key路径查找对应值
     *
     * @param key
     * @param <T>
     * @return null:没有对应的值；不为null：对应的值
     */
    @Nullable
    public <T> T getValue(String... key) {
        if (key == null || key.length == 0) return null;
        Object value = getObj(key);
        if (value == null) return null;
        return (T) value;
    }

    /**
     * 获取对应的Object
     * @param key
     * @return
     */
    @Nullable
    private Object getObj(String... key) {
        int index = 0;
        HashMap<String, Object> temp = null;
        Object obj = properties.get(key[index]);
        while (obj instanceof HashMap && index + 1 < key.length) {
            temp = (HashMap<String, Object>) obj;
            index++;
            obj = temp.get(index);

        }
        return obj;
    }

}
