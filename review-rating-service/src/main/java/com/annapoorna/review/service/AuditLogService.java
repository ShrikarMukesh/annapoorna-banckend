package com.annapoorna.review.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.annapoorna.review.entity.AuditLog;
import com.annapoorna.review.repository.AuditLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    @Value("${spring.application.name:review-rating-service}")
    private String serviceName;

    @Async
    public void logSuccess(String eventType, String entityType, String entityId,
            String userId, Object oldValue, Object newValue,
            String description, String ipAddress) {
        try {
            AuditLog auditLog = AuditLog.builder()
                    .serviceName(serviceName)
                    .eventType(eventType)
                    .affectedEntityType(entityType)
                    .affectedEntityId(entityId)
                    .userId(userId)
                    .oldValue(toJson(oldValue))
                    .newValue(toJson(newValue))
                    .statusCode(200)
                    .description(description)
                    .ipAddress(ipAddress)
                    .build();

            auditLogRepository.save(auditLog);
            log.debug("Audit log saved: {} - {} - {}", eventType, entityType, entityId);
        } catch (Exception e) {
            log.error("Failed to save audit log: {}", e.getMessage(), e);
        }
    }

    @Async
    public void logError(String eventType, String entityType, String entityId,
            String userId, Integer statusCode, String errorMessage,
            String requestPayload, String ipAddress) {
        try {
            AuditLog auditLog = AuditLog.builder()
                    .serviceName(serviceName)
                    .eventType(eventType)
                    .affectedEntityType(entityType)
                    .affectedEntityId(entityId)
                    .userId(userId)
                    .statusCode(statusCode)
                    .errorMessage(errorMessage)
                    .requestPayload(requestPayload)
                    .ipAddress(ipAddress)
                    .build();

            auditLogRepository.save(auditLog);
            log.debug("Audit error log saved: {} - {} - {}", eventType, entityType, errorMessage);
        } catch (Exception e) {
            log.error("Failed to save audit error log: {}", e.getMessage(), e);
        }
    }

    private String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize object to JSON: {}", e.getMessage());
            return obj.toString();
        }
    }
}
