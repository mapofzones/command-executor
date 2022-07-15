package com.mapofzones.commandexecutor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class Processor {
    public Processor() {

    }

    public void doScript() {
        log.info("----------Starting iteration...");
        final String bashLocation = "/bin/sh";
        final String bashOption = "-c";
        String cleanupCrashLoopBackOffPodsCommand = "kubectl -n prod delete pod `kubectl -n prod get pods | awk '$3 == \"CrashLoopBackOff\" {print $1}'`";
        String deletePodByLabelCommandPattern = "kubectl -n prod delete pods -l app=<app_name>";

        for (String label : Constants.watchersLabels) {
            log.info("Starting sub-loop iteration...");
            try {
                log.info("Started CrashLoopBackOff cleanup");
                ArrayList<String> cleanupCrashLoopBackOffPods = prepareCommand(bashLocation, bashOption, cleanupCrashLoopBackOffPodsCommand);
                List<String> results = executeCommand(cleanupCrashLoopBackOffPods.toArray(new String[0]));
                logExecutionResults(results);
                log.info("Finished CrashLoopBackOff cleanup");

                log.info("Started watcher cleanup");
                ArrayList<String> deletePodByLabel = prepareCommand(bashLocation, bashOption, deletePodByLabelCommandPattern.replace("<app_name>", label));
                List<String> results2 = executeCommand(deletePodByLabel.toArray(new String[0]));
                logExecutionResults(results2);
                log.info("Finished watcher cleanup");

                log.info("Finished sub-loop iteration...");
                Thread.sleep(30000L);//30 sec
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("----------Finished iteration...");
    }

    private ArrayList<String> prepareCommand(String bashLocation, String bashOption, String deletePodByLabelCommand) {
        return new ArrayList<>() {
            {
                add(bashLocation);
                add(bashOption);
                add(deletePodByLabelCommand);
            }
        };
    }

    private void logExecutionResults(List<String> results) {
        for (String result : results) {
            log.info(result);
        }
    }

    private List<String> executeCommand(String[] arguments) throws IOException {
        Process proc = new ProcessBuilder(arguments).start();
        return getExecutionResults(proc);
    }

    public static List<String> getExecutionResults(Process process) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return reader.lines().collect(Collectors.toList());
    }
}
