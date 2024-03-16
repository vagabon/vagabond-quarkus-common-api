package org.vagabond.common.notification.payload;

import java.util.List;

public class NotificationRequest {
    public String title;
    public String body;
    public String url;
    public List<String> tokens;

    public NotificationRequest(String title, String body, String url) {
        this.title = title;
        this.body = body;
        this.url = url;
    }
}
