package com.wyl.util;


import static com.wyl.util.TextUtil.isEmpty;

/**
 * @author : yuelinwang
 * @time : 12/28/21
 * @desc : 用来检查方法的条件
 */
public class Preconditions {
    private Preconditions() {
    }

    /**
     * 检查参数是否合法
     * @param condition
     * @param msg
     */
    public static void checkArgument(boolean condition, String msg) {
        if (!condition) {
            throw isEmpty(msg) ? new IllegalArgumentException() : new IllegalArgumentException(msg);
        }
    }

    public static void checkArgument(boolean condition) {
        checkArgument(condition, null);
    }

    /**
     * 检查状态
     * @param condition
     * @param msg
     */
    public static void checkState(boolean condition, String msg) {
        if (!condition) {
            throw isEmpty(msg) ? new IllegalStateException() : new IllegalStateException(msg);
        }
    }

}
