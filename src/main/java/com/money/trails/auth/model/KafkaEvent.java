package com.money.trails.auth.model;

import lombok.Builder;

@Builder
public record KafkaEvent(String type, String event) {
}