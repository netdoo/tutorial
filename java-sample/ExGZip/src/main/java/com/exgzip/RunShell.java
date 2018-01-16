package com.exgzip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RunShell {

    public static void RunSh(String shPath, List<String> parameter, boolean isWait) {
        String shell = "";
        List<String> cmd = new ArrayList<>();
        cmd.add("/bin/sh");
        cmd.add("-c");
        shell = shPath;
        for(String param : parameter){
            shell += " " + getParam(param);
        }
        cmd.add(shell);
        ProcessBuilder bld = new ProcessBuilder(cmd);

        Process process = null;
        try {
            process = bld.start();
            if(isWait){
                if (process.waitFor(5 * 60 * 1000, TimeUnit.MILLISECONDS)) {
                    String str;
                    // 외부 프로그램 출력 읽기
                    BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                    // "표준 출력"과 "표준 에러 출력"을 출력
                    while ((str = stdOut.readLine()) != null) {
                        logger.info("Shell Process info : {} ",str);
                    }
                    while ((str = stdError.readLine()) != null) {
                        logger.error("Shell Error info : {} ",str);
                    }

                    // 외부 프로그램 반환값 출력 (이 부분은 필수가 아님)
                    logger.info("Exit Code: " + process.exitValue());
                } else {
                    logger.warn("shell이 5분 이상 실행되고 있어 종료함");
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Run Shell Error", e);
        } finally {
            if (process != null && process.isAlive()) {
                process.destroy();
            }
            logger.info("RUN SHELL END");
        }
    }

    private static String getParam(String param){
        return "\"" + param + "\"";
    }

    private static final Logger logger = LoggerFactory.getLogger(RunShell.class);
}

