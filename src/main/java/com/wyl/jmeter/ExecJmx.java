package com.wyl.jmeter;

import com.sun.deploy.util.StringUtils;
import com.wyl.util.FileUtil;
import com.wyl.util.Logger;
import com.wyl.util.Preconditions;
import com.wyl.util.TextUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2022/4/19
 * 描述    : 执行jmx文件
 */
public class ExecJmx {
    private static final String successEndIdentifier = "... end of run";
    private String jmxPath; // jmx文件路劲
    private String resultDir; // 存放执行结果的目录
    private String jmeterRoot;// jmeter 目录
    private ThreadGroup threadGroup;// 线程组的信息
    private String resultPath; // 存储结果的文件
    private String reportDir; // 报告的目录


    public ExecJmx(String jmxPath, String resultDir, String jmeterRoot, ThreadGroup threadGroup) {
        Preconditions.checkArgument(!TextUtil.isEmpty(resultDir), "存放结果的目录不能为空");

        this.jmxPath = jmxPath;
        this.resultDir = resultDir;
        this.jmeterRoot = jmeterRoot;
        this.threadGroup = threadGroup;
        resultPath = resultDir + "result" + File.separator + "result.txt";
        reportDir = resultDir + File.separator + "webreport" + File.separator;
    }

    public boolean exec() {
        try {
            if (!ensureExistAndClean(resultPath, false)
                    || !ensureExistAndClean(reportDir, true)) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // 开始命令行执行
        String[] cmd = new String[]{jmeterRoot + File.separator + "bin" + File.separator + "jmeter", "-n", "-t", jmxPath, "-l", resultPath, "-e", "-o", reportDir};
        String line = "";
        try {
            Process ps = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            line = sb.toString();
            Logger.d("命令执行的结果：\n" );
            Logger.d(line);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
       return line.contains(successEndIdentifier);
    }

    /**
     * 确保干净的存在：对于文件：存在则清空，不存在则新建；对于文件夹：存在则清空对应目录下文件，不存在则新建目录
     * @param path
     * @return
     */
    private boolean ensureExistAndClean(String path, boolean isDir) throws IOException {
        if (path == null || path.isEmpty()) return false;
        File f = new File(path);
        if (isDir) { // 文件夹
            if (f.exists()) {
                File[] files = f.listFiles();
                for (File file : files) {
                    FileUtil.remove(file);
                }
                return true;
            } else {
                return f.mkdirs();
            }
        } else { // 文件
            if (f.exists()) {
                FileUtil.remove(f);
            }
            File parent = new File(f.getParent());
            if (!parent.exists()) {
                if (!parent.mkdirs()) {
                    return false;
                }
            }
            return f.createNewFile();
        }
    }

}
