package com.sazakimaeda.h2.service.impl;

import com.sazakimaeda.h2.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsoleAuditService implements AuditService {

    @Override
    public void send(String audit) {
        log.info("[WEYLAND-YUTANI]: {}", audit);
    }
}
