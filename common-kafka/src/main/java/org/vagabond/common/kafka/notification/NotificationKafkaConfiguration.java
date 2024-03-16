package org.vagabond.common.kafka.notification;

import java.io.IOException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.vagabond.common.notification.kafka.NotificationKafkaService;
import org.vagabond.common.notification.payload.NotificationRequest;

import io.quarkus.logging.Log;

@ApplicationScoped
public class NotificationKafkaConfiguration {

    public FirebaseMessaging messaging;

    @Inject
    NotificationKafkaService notificationKafkaService;

    public NotificationKafkaConfiguration() {

        var serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase-account-service.json");
        FirebaseOptions options = null;
        try {
            options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

        } catch (IOException e) {
            Log.error(e);
            return;
        }
        FirebaseApp app = null;
        try {
            app = FirebaseApp.initializeApp(options, "blogui");
        } catch (IllegalStateException e) {
            app = FirebaseApp.getInstance("blogui");
        }
        messaging = FirebaseMessaging.getInstance(app);
    }

    @Incoming("notification")
    public void consume(NotificationRequest notification) {
        Log.infof("kafka receive notification %s", notification);

        if (!notification.tokens.isEmpty()) {
            var notificationToSend = Notification.builder().setTitle(notification.title).setBody(notification.body).build();
            var message = MulticastMessage.builder().addAllTokens(notification.tokens).setNotification(notificationToSend)
                    .putData("url", notification.url).build();

            try {
                messaging.sendMulticast(message);
            } catch (FirebaseMessagingException exception) {
                Log.error(exception);
            }
        }

    }
}
