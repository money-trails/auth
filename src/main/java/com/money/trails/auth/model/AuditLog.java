package com.money.trails.auth.model;

import com.money.trails.auth.conatants.AuditLogStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String event;

    @Column(length = 5000)
    private String message;

    @Enumerated(EnumType.STRING)
    private AuditLogStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant timestamp;
}
