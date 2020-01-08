package com.IITBhilai.techmeet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogsUtil {
    static int points=0;
    static long last=-10000;
//    static long f=0;


    public static int readLogs() {
        StringBuilder logBuilder = new StringBuilder();
//        if(f==0){
//            System.out.println("################");
//            f=System.currentTimeMillis();
//        }

//        if(System.currentTimeMillis()-f<=2000)
//            return 0;
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            Runtime.getRuntime().exec("logcat -c");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line, ar[];
            while ((line = bufferedReader.readLine()) != null) {
                logBuilder.append(line + "\n");
                ar=logBuilder.toString().split("\n");
                for(String s: ar){
                    if(s.contains("pothole")){
                        if(System.currentTimeMillis()-last>10000){
                            System.out.println("Found "+points);
                            points++;
                            last=System.currentTimeMillis();
                            if(points==1)
                                return 0;
                            return 1;
                        }
                        else
                            System.out.println("Found Duplicate");
                        last=System.currentTimeMillis();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
