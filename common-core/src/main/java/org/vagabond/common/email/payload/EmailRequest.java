package org.vagabond.common.email.payload;

import org.vagabond.common.user.UserEntity;

public record EmailRequest(String to, String subject, String html, UserEntity user) {

}
