package com.hihooda.common.utils;

import java.io.*;

public class CUtil {
    private static String getName() {
        return System.getProperty("os.name").toLowerCase();
    }

    private static String getCPUID_Windows() {
        String result = "";
        try {
            File file = File.createTempFile("tmp", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + "   (\"Select * from Win32_Processor\") \n"
                    + "For Each objItem in colItems \n"
                    + "    Wscript.Echo objItem.ProcessorId \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";

            // + "    exit for  \r\n" + "Next";
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec(
                    "cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
            file.delete();
        } catch (Exception e) {
        }
        return result.trim();
    }

    private static String getCPUID_linux() throws InterruptedException {
        String result = "";
        String CPU_ID_CMD = "dmidecode";
        BufferedReader bufferedReader = null;
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(new String[]{ "sh", "-c", CPU_ID_CMD });// 管道
            bufferedReader = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line = null;
            int index = -1;
            while ((line = bufferedReader.readLine()) != null) {
                index = line.toLowerCase().indexOf("uuid");
                if (index >= 0) {
                    result = line.substring(index + "uuid".length() + 1).trim();
                    break;
                }
            }

        } catch (IOException e) {
        }
        return result.trim();
    }

    private static String getCPUId() throws InterruptedException {
        String os = getName();
        String cpuId = "";
        if (os.startsWith("windows")) {
            cpuId = CUtil.getCPUID_Windows();
        } else if (os.startsWith("linux")) {
            cpuId = CUtil.getCPUID_linux();
        }
//        if(!StringUtil.isNotNullOrBlank(cpuId)){
//            cpuId="null";
//        }
        return cpuId;
    }

    public static String get(){
        try {
            return getCPUId();
        } catch (InterruptedException e) {
            return "error";
        }
    }
}
