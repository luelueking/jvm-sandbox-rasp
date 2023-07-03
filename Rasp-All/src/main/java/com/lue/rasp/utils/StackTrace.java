package com.lue.rasp.utils;

import com.lue.rasp.context.RequestContextHolder;

/**
 * 调用栈
 */
public class StackTrace {

    /**
     * RASP自身的栈开始位置
     */
    private final static String RASP_STACK_END = "java.com.alibaba.jvm.sandbox.spy";

    public static void logTraceWithContext(RequestContextHolder.Context context) {
        System.out.println("开始打印危险route");
        System.out.println(context);
        System.out.println(context.getRequest().getRequestURI());
        logTrace();
    }
    public static void logTrace() {
        System.out.println("开始打印StackTrace");
        for (String s : StackTrace.getStackTraceString()) {
            System.out.println(s);
        }
    }

    public static String[] getStackTraceString() {
        return getStackTraceString(100,true);
    }
    /**
     * @param maxStack 输出的最大栈深度
     * @return
     */
    public static String[] getStackTraceString(int maxStack, boolean hasLineNumber) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        // 找到用户栈开始index
        int i;
        for (i = 0; i < stackTraceElements.length; i++) {
            String className = stackTraceElements[i].getClassName();
            if (className != null && className.startsWith(RASP_STACK_END)) {
                break;
            }
        }
        int endIndex = Math.min(i + maxStack, stackTraceElements.length - 1);
        String[] effectiveArray = new String[endIndex - i];
        // 获取有用的栈
        for (int k = i + 1; k <= endIndex; k++) {
            String info = "";
            StackTraceElement tmp = stackTraceElements[k];
            if (hasLineNumber) {
                // 不包含行号
                info = tmp.toString();
            } else {
                info = tmp.getClassName() + "." + tmp.getMethodName();
            }
            effectiveArray[k - i - 1] = info;
        }
        return effectiveArray;
    }

    /**
     * @param maxStack 输出的最大栈深度
     * @return
     */
    public static StackTraceElement[] getStackTraceObject(int maxStack) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        // 找到用户栈开始index
        int i;
        for (i = 0; i < stackTraceElements.length; i++) {
            String className = stackTraceElements[i].getClassName();
            if (className != null && className.startsWith(RASP_STACK_END)) {
                break;
            }
        }
        int endIndex = Math.min(i + maxStack, stackTraceElements.length - 1);
        StackTraceElement[] effectiveArray = new StackTraceElement[endIndex - i];
        // 获取有用的栈
        for (int k = i + 1; k <= endIndex; k++) {
            effectiveArray[k - i - 1] = stackTraceElements[k];
        }
        return effectiveArray;
    }

}
