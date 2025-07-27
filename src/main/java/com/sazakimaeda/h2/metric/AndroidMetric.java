package com.sazakimaeda.h2.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AndroidMetric {
    private static final String METRIC_TASKS_COMPLETED = "android.tasks.completed.total";
    private static final String METRIC_QUEUE_SIZE = "android.queue.size";
    private static final String AUTHOR_TAG = "Author";

    private final MeterRegistry meterRegistry;
    private final ThreadPoolExecutor threadPoolExecutor;


    @PostConstruct
    public void init() {
        if (threadPoolExecutor != null) {
            try {
                Gauge.builder(METRIC_QUEUE_SIZE, threadPoolExecutor,
                                executor -> executor.getQueue().size())
                        .description("Текущее количество команд в очереди COMMON приоритета")
                        .strongReference(true)
                        .register(meterRegistry);
                log.info("Метрика '{}' успешно зарегистрирована", METRIC_QUEUE_SIZE);
            } catch (Exception e) {
                log.error("Ошибка при регистрации метрики размера очереди", e);
            }
        } else {
            log.warn("ThreadPoolExecutor не предоставлен. Метрика размера очереди недоступна.");
        }
    }

    public void incrementTasks(String author) {
        try {
            Counter.builder(METRIC_TASKS_COMPLETED)
                    .description("Общее количество выполненных команд по авторам")
                    .tag(AUTHOR_TAG, author != null ? author : "неизвестно")
                    .register(meterRegistry)
                    .increment();
        } catch (Exception e) {
            log.warn("Ошибка при регистрации выполненной задачи для автора: {}", author, e);
        }
    }
}