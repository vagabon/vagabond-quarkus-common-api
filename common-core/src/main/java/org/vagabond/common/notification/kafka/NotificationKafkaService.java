package org.vagabond.common.notification.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.vagabond.common.notification.payload.NotificationRequest;

import io.smallrye.reactive.messaging.annotations.Broadcast;

@ApplicationScoped
public class NotificationKafkaService {

    @Inject
    @Channel("notification-out")
    @Broadcast
    Emitter<NotificationRequest> notificationEmitter;

    public NotificationRequest registerNotification(NotificationRequest notification) {
        notificationEmitter.send(notification);
        return notification;
    }
}
