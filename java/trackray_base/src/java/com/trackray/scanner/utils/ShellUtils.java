package com.trackray.scanner.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        String result = exeBlockShell("D:/nmap/nmap -O 54.69.217.239");
        System.out.print(result);
    }

    public static String[] filter(String... str){
        return str;
    }
}
