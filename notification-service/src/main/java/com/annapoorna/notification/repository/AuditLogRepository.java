package com.annapoorna.notification.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.annapoorna.notification.entity.AuditLog;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {

    List<AuditLog> findByServiceName(String serviceName);

    List<AuditLog> findByEventType(String eventType);

    List<AuditLog> findByUserId(String userId);

    List<AuditLog> findByAffectedEntityTypeAndAffectedEntityId(String entityType, String entityId);

    List<AuditLog> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<AuditLog> findByStatusCode(Integer statusCode);
}
