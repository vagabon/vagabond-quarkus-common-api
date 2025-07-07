package org.vagabond.common.kafka.notification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.vagabond.common.notification.payload.NotificationRequest;

import io.quarkus.logging.Log;

@ApplicationScoped
public class NotificationKafkaConfiguration {

    @ConfigProperty(name = "firebase.name")
    private String firebaseName;

    @ConfigProperty(name = "firebase.path", defaultValue = "localhost")
    private String firebasePath;

    private FirebaseMessaging messaging;

    @PostConstruct
    public void postConstruct() throws IOException {

        var serviceAccount = Files.newInputStream(Paths.get(firebasePath));
        FirebaseOptions options = null;
        try {
            options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

        } catch (IOException e) {
            Log.error(e);
            return;
        }
        FirebaseApp app = null;
        try {
            app = FirebaseApp.initializeApp(options, firebaseName);
        } catch (IllegalStateException e) {
            app = FirebaseApp.getInstance(firebaseName);
        }
        messaging = FirebaseMessaging.getInstance(app);
    }

    @Incoming("notification")
    public void consume(NotificationRequest notification) {
        Log.infof("kafka receive notification %s %s", notification.title, notification.tokens);

        if (!notification.tokens.isEmpty()) {
            var notificationToSend = Notification.builder().setTitle(notification.title).setBody(notification.body).build();
            var message = MulticastMessage.builder().addAllTokens(notification.tokens).setNotification(notificationToSend)
                    .putData("url", notification.url).build();

            try {
                messaging.sendEachForMulticast(message);
            } catch (FirebaseMessagingException exception) {
                Log.error(exception);
            }
        }

    }
}
