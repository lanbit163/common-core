package com.hihooda.common.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class Target {
    public static final int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0]).intValue();
    }

}
