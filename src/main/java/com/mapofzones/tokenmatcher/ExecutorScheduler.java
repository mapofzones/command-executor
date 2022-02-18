package com.mapofzones.tokenmatcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExecutorScheduler {
    private final Processor processor;

    public ExecutorScheduler(Processor processor) {
        this.processor = processor;
    }

    @Scheduled(fixedDelayString = "${executor.sync-time}")
    public void callProcessor() {
        processor.doScript();
    }
}
