package org.vagabond.common.notification.payload;

import java.util.List;

import org.vagabond.engine.user.CurrentUser;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotificationRequest {
    public String title;
    public String body;
    public String url;
    public CurrentUser userConnected;
    public List<Long> userIds;
    public List<String> tokens;

    public NotificationRequest(String title, String body) {
        this.title = title;
        this.body = body;
        this.url = "/notification";
    }

    public NotificationRequest(
        String title,
        String body,
        String url,
        CurrentUser userConnected,
        List<Long> userIds) {
        this.title = title;
        this.body = body;
        this.url = url;
        this.userConnected = userConnected;
        this.userIds = userIds;
    }
}
