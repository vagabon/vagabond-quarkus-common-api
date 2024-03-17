package org.vagabond.common.notification.payload;

import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotificationRequest {
    public String title;
    public String body;
    public String url;
    public List<String> tokens;

    public NotificationRequest(String title, String body) {
        this.title = title;
        this.body = body;
        this.url = "/notification";
    }

    public NotificationRequest(String title, String body, String url) {
        this.title = title;
        this.body = body;
        this.url = url;
    }
}
