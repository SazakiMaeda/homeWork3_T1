package com.sazakimaeda.h2.service.impl;

import com.sazakimaeda.h2.handler.exception.QueueIsFullException;
import com.sazakimaeda.h2.metric.AndroidMetric;
import com.sazakimaeda.h2.model.Command;
import com.sazakimaeda.h2.model.Priority;
import com.sazakimaeda.h2.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {

    private final ExecutorService executorService;
    private final AndroidMetric androidMetric;

    @Override
    public void executeCommand(Command command) {
        if (command.getPriority() == Priority.CRITICAL) {
            executeCommand(command);
        } else {
            executeCommand(command);
        }
    }

    private void executeCriticalCommand(Command command) {
        CompletableFuture.runAsync(() -> {
            log.info("Выполнение команды CRITICAL, автора {}, описание: {}",
                    command.getAuthor(), command.getDescription());
            androidMetric.incrementTasks(command.getAuthor());
        }).exceptionally(e -> {
            log.error("ОШИБКА выполнения команды CRITICAL, автор {}, ошибка: {}",
                    command.getAuthor(), e.getMessage(), e);
            return null;
        });
    }

    private void executeCommonCommand(Command command) {
        try {
            executorService.submit(() -> {
                log.info("Выполнение команды COMMON, автора {}, описание: {}",
                        command.getAuthor(), command.getDescription());
                androidMetric.incrementTasks(command.getAuthor());
            });
        } catch (QueueIsFullException e) {
            throw new QueueIsFullException();
        }
    }
}
