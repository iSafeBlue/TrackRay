package com.trackray.base.utils;



public class ShellUtils {

    public static String exeBlockShell(String commandStr){
        Process process;
        String analysisProcess = null;
        try {
            process = Runtime.getRuntime().exec(commandStr);
            process.getInputStream();
            analysisProcess = IOUtils.analysisProcess(process);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return analysisProcess;
    }


    public static void main(String[] args) {

    }

    public static String[] filter(String... str){
        return str;
    }
}
