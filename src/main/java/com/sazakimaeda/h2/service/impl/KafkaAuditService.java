package com.sazakimaeda.h2.service.impl;

import com.sazakimaeda.h2.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaAuditService implements AuditService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.audit-topic}")
    private String topic;

    @Override
    public void send(String audit) {
        try {
            kafkaTemplate.send(topic, audit);
        } catch (Exception e) {
            log.error("Увы, не отправили через Кафку в топик - {}, {}",
                    topic, e.getMessage());
        }
    }
}
