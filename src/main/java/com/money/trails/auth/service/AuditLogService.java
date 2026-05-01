package com.money.trails.auth.service;

import com.money.trails.auth.model.AuditLog;
import com.money.trails.auth.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void log(AuditLog auditLog) {
        auditLogRepository.save(auditLog);
    }
}