package com.money.trails.auth.kafka;

import com.money.trails.auth.conatants.AuditLogStatus;
import com.money.trails.auth.config.KafkaProducerConfig;
import com.money.trails.auth.model.AuditLog;
import com.money.trails.auth.model.KafkaEvent;
import com.money.trails.auth.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyProducerService {

    private final AuditLogService auditLogService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProducerConfig kafkaProducerConfig;

    public void publishNotify(KafkaEvent event) {

        kafkaTemplate.send(kafkaProducerConfig.getNotifyTopic(), event.type(), event)
                .thenAccept(result -> {
                            int partition = result.getRecordMetadata().partition();
                            long offset = result.getRecordMetadata().offset();
                            log.info("Kafka event published on partition={} offset={}", partition, offset);
                            auditLogService.log(AuditLog.builder()
                                    .event("KAFKA-PRODUCER:" + event.type())
                                    .message("Kafka event published on partition=" + partition + " offset=" + offset)
                                    .status(AuditLogStatus.SUCCESS)
                                    .build());
                        }
                ).exceptionally(ex -> {
                    log.error("Kafka event published failed message={}", ex.getMessage());
                    auditLogService.log(AuditLog.builder()
                            .event("KAFKA")
                            .message("Kafka event published failed message=" + ex.getMessage())
                            .status(AuditLogStatus.FAILED)
                            .build());
                    return null;
                });
    }
}