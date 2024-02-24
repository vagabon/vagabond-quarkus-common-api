package org.vagabond.engine.email.kafka.payload;

public record EmailKafkaRequest(String to, String subject, String html) {

}
