package com.mapofzones.tokenmatcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Service
public class Processor {
    public Processor() {

    }

    public void doScript() {
        log.info("Starting...");
        String[] arguments = new String[] {"/bin/sh", "-c", "kubectl -n prod delete pod `kubectl -n prod get pods | awk '$3 == \"CrashLoopBackOff\" {print $1}'`"};
//        String[] arguments = new String[] {"/bin/sh", "-c", "kubectl -n prod get pods | awk '$3 == \"CrashLoopBackOff\" {print $1}'"};
//        String[] arguments = new String[] {"kubectl", "get", "namespaces"};
        try {
            Process proc = new ProcessBuilder(arguments).start();
            Thread.sleep(30000L);//30 sec
            printResults(proc);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Finished...");
    }

    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            log.info(line);
        }
    }
}
