package com.wyl.conf;

import junit.framework.TestCase;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static java.net.URLDecoder.decode;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2022/4/19
 * 描述    :
 */
public class ConfigTest extends TestCase {
    private static final String rpojectName = "jmeter_helper";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        String configPath = null;
        try {
            configPath = getConfigPath();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        Config.getInstance().init(configPath);

    }

    public void testGetValue() {
//        Object obj = Config.getInstance().getValue("thread_group");
//        assertTrue(obj instanceof List);

        File file = new File("/Users/yuelinwang/Downloads/testplan/plan.jmx");
        assertTrue(file.exists());

    }

    private String getConfigPath() throws UnsupportedEncodingException {
        return getRootPath() + File.separator + "conf" + File.separator + "config.yaml";
    }

    private String getRootPath() throws UnsupportedEncodingException {
        String path = this.getClass().getResource("/").getPath();
        int index = path.indexOf(rpojectName);
        if (index < 0) {
            throw new IllegalStateException("项目的名称不对，应该为:" + rpojectName);
        }
        return decode(path.substring(0, index + rpojectName.length()), "UTF-8");
    }
}