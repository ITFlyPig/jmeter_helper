package com.wyl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.wyl.conf.Config;
import com.wyl.jmeter.ExecJmx;
import com.wyl.jmeter.ExecJmxFactory;
import com.wyl.jmeter.ThreadGroup;
import com.wyl.util.Logger;
import com.wyl.xml.XmlFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.net.URLDecoder.decode;

/**
 * Hello world!
 */
public class App {
    private static final String rpojectName = "jmeter_helper";

    public static void main(String[] args) {
        Logger.d("程序开始执行");
        try {
            String configPath = new App().getConfigPath();
            Config.getInstance().init(configPath);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String jmeterDir = Config.getInstance().getValue("jmeter_dir");
        String jmxPath = Config.getInstance().getValue("jmx_path");
        String resultDir = Config.getInstance().getValue("result_dir");
        List<Object> groups = Config.getInstance().getValue("thread_group");
        if (groups == null) {
            Logger.d("获取的thread_group为空，退出执行");
            return;
        }

        for (Object group : groups) {
            if (!(group instanceof HashMap)) {
                continue;
            }
            HashMap<String, Object> map = (HashMap<String, Object>) group;
            String name = (String) map.get("name");
            int threadNum = getInt(map, "thread_num");
            int repeatNum = getInt(map, "repeat_num");
            ThreadGroup threadGroup = new ThreadGroup(name, threadNum, repeatNum);
            String newJmx = saveNewJmx(threadGroup, jmxPath);
            if (newJmx == null) {
                Logger.d("创建" + name + "对应的新Jmx文件失败，跳过该任务的执行");
                continue;
            }
            new File(newJmx).deleteOnExit(); // 退出时清除临时文件

            ExecJmx execJmx = ExecJmxFactory.create(threadGroup, resultDir, newJmx, jmeterDir);
            if (execJmx == null) {
                Logger.d("创建" + name + "对应的ExecJmx失败，跳过该任务的执行");
                continue;
            }
            Logger.d("\n========================\n开始处理：" + name);
            boolean success = execJmx.exec();
            Logger.d(name + " 任务执行" + (success ? "成功" : "失败"));
            Logger.d("========================");
        }
        Logger.d("程序执行完成");
    }

    /**
     * 据group修改原始originJmxPath文件中的属性保存到新文件
     *
     * @param group
     * @param originJmxPath
     * @return
     */
    @Nullable
    private static String saveNewJmx(@NotNull ThreadGroup group, @NotNull String originJmxPath) {
        XmlFile xmlFile = new XmlFile(originJmxPath);
        if (!xmlFile.read()) {
            return null;
        }
        // 修改并发的线程数
        xmlFile.set("//ThreadGroup/stringProp[@name='ThreadGroup.num_threads']", String.valueOf(group.getThreadNum()));
        // 设置重复的次数
        xmlFile.set("//ThreadGroup/elementProp[@name='ThreadGroup.main_controller']/stringProp[@name='LoopController.loops']", String.valueOf(group.getRepeatNum()));
        File origin = new File(originJmxPath);
        String newJmxPath = origin.getParent() + File.separator + group.getName() + ".jmx";
        return xmlFile.save(newJmxPath) ? newJmxPath : null;
    }


    private static <T> T getObj(Map<String, Object> map, String key) {
        Object obj = map.get(key);
        if (obj == null) return null;
        return (T) obj;
    }

    private static int getInt(Map<String, Object> map, String key) {
        Object obj = map.get(key);
        if (obj instanceof Integer) {
            return (int) obj;
        } else {
            return -1;
        }
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
