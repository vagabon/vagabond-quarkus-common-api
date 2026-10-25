package org.vagabond.engine.kafka.service;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Message;

public interface IKafkaService<T> {

    CompletionStage<Void> consumeIncoming(Message<T> entity);

}
