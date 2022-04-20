package com.wyl.jmeter;

import com.sun.istack.internal.Nullable;
import com.wyl.conf.Config;
import com.wyl.util.FileUtil;
import com.wyl.util.Preconditions;
import com.wyl.util.TextUtil;

import java.io.File;
import java.io.IOException;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2022/4/19
 * 描述    :
 */
public class ExecJmxFactory {

    @Nullable
    public static ExecJmx create(ThreadGroup threadGroup, String resultDir, String jmxPath, String jmeterRoot) {
        Preconditions.checkArgument(new File(jmxPath).exists(), "jmx文件必须存在");
        Preconditions.checkArgument(new File(jmeterRoot).exists(), "jmeter安装目录不存在");
        Preconditions.checkArgument(threadGroup != null, "参数threadGroup不能为空");
        Preconditions.checkArgument(!TextUtil.isEmpty(threadGroup.getName()), "ThreadGroup的name不能为空");
        Preconditions.checkArgument(threadGroup.getThreadNum() > 0, "ThreadGroup的threadNum不能小于0");
        Preconditions.checkArgument(threadGroup.getRepeatNum() > 0, "ThreadGroup的repeatNum不能小于0");

        String resultPath = resultDir + File.separator + threadGroup.getName() + File.separator;
        if (!FileUtil.makeSureExits(resultPath, true)) {
            return null;
        }
        ExecJmx execJmx = new ExecJmx(jmxPath, resultPath, jmeterRoot, threadGroup);
        return execJmx;

    }



}
