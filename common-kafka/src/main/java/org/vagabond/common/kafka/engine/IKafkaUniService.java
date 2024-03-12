package org.vagabond.common.kafka.engine;

import org.eclipse.microprofile.reactive.messaging.Message;

import io.smallrye.mutiny.Uni;

public interface IKafkaUniService<T> {

    Uni<Void> consumeIncoming(Message<T> entity);

}
