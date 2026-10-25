package org.vagabond.common.email.payload;

import org.vagabond.engine.user.CurrentUser;

public record EmailRequest(
    String to,
    String subject,
    String html,
    CurrentUser user) {

}
