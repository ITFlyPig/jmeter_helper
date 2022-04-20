package com.wyl.jmeter;

import com.wyl.util.Preconditions;
import com.wyl.util.TextUtil;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2022/4/19
 * 描述    :
 */
public class ThreadGroup {
    private String name;
    private int threadNum;
    private int repeatNum;

    public ThreadGroup(String name, int threadNum, int repeatNum) {
        // 做参数的校验
        Preconditions.checkArgument(!TextUtil.isEmpty(name), "name参数不能为空");
        Preconditions.checkArgument(threadNum > 0, "参数threadNum必须大于0");
        Preconditions.checkArgument(repeatNum > 0, "参数repeatNum必须大于0");
        this.name = name;
        this.threadNum = threadNum;
        this.repeatNum = repeatNum;
    }

    public String getName() {
        return name;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public int getRepeatNum() {
        return repeatNum;
    }
}
