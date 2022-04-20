package com.wyl.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static java.net.URLDecoder.decode;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2022/4/19
 * 描述    : 文件工具
 */
public class FileUtil {
    /**
     * 确保干净的存在：对于文件：存在则清空，不存在则新建；对于文件夹：存在则清空对应目录下文件，不存在则新建目录
     * @param path
     * @return
     */
    public static boolean ensureExistAndClean(String path) throws IOException {
        if (path == null || path.isEmpty()) return false;
        File f = new File(path);
        if (f.isDirectory()) { // 文件夹
            if (f.exists()) {
                File[] files = f.listFiles();
                for (File file : files) {
                    file.deleteOnExit();
                }
            } else {
                return f.mkdirs();
            }
        } else { // 文件
            f.deleteOnExit();
            return f.createNewFile();
        }
        return false;
    }

    /**
     * 确保文件或者目录存在
     * @param path
     * @return
     */
    public static boolean makeSureExits(String path, boolean isDir) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                return isDir ? file.mkdirs() : file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return true;
        }
    }

    public static boolean remove(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (File file : files) {
                boolean success = remove(file);
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return f.delete();
    }
}
