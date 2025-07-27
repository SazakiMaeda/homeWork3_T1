package com.sazakimaeda.h2.configuration;


import com.sazakimaeda.h2.aspect.AuditAspect;
import com.sazakimaeda.h2.metric.AndroidMetric;
import com.sazakimaeda.h2.service.AuditService;
import com.sazakimaeda.h2.service.CommandService;
import com.sazakimaeda.h2.service.impl.CommandServiceImpl;
import com.sazakimaeda.h2.service.impl.ConsoleAuditService;
import com.sazakimaeda.h2.service.impl.KafkaAuditService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
public class WeylandYutaniAutoConfiguration {

    private int poolSize = 1;
    private int maxPoolSize = 1;
    private int queueCapacity = 100;


    @Bean
    @ConditionalOnMissingBean
    public ThreadPoolExecutor commonCommandExecutor() {
        return new ThreadPoolExecutor(
                poolSize,
                maxPoolSize,
                60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueCapacity)
        );
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(MeterRegistry.class)
    public AndroidMetric androidMetricsRecorder(MeterRegistry meterRegistry,
                                                ThreadPoolExecutor threadPoolExecutor) {
        return new AndroidMetric(meterRegistry, threadPoolExecutor);
    }

    @Bean
    @ConditionalOnMissingBean
    public CommandService commandExecutorService(
            ThreadPoolExecutor commonCommandExecutor,
            ObjectProvider<AndroidMetric> metricsRecorder) {
        return new CommandServiceImpl(commonCommandExecutor, metricsRecorder.getIfAvailable());
    }


    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "org.aspectj.lang.annotation.Aspect")
    static class AuditConfig {

        @Bean
        @ConditionalOnMissingBean
        public AuditService auditSender(ObjectProvider<KafkaTemplate<String, String>> kafkaTemplate) {
            return kafkaTemplate.getIfAvailable() != null
                    ? new KafkaAuditService(kafkaTemplate.getIfAvailable())
                    : new ConsoleAuditService();
        }

        @Bean
        @ConditionalOnMissingBean
        public AuditAspect auditAspect(@Qualifier("consoleAuditService") AuditService auditService) {
            return new AuditAspect(auditService);
        }
    }
}
