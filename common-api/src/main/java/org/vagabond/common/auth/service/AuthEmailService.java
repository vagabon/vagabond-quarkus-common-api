package org.vagabond.common.auth.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.vagabond.common.email.EmailService;
import org.vagabond.common.email.payload.EmailRequest;
import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.email.builder.EmailBuilder;
import org.vagabond.engine.email.dto.ResponseBodyDto;

@ApplicationScoped
public class AuthEmailService {

    private static final String WEBSITE_NAME = "${WEBSITE_NAME}";
    private static final String WEBSITE_URL = "${WEBSITE_URL}";

    @ConfigProperty(name = "website.name", defaultValue = "NO_WEBSITE_NAME")

    private String websiteName;
    @ConfigProperty(name = "website.url", defaultValue = "NO_WEBSITE_URL")

    private String websiteUrl;
    @ConfigProperty(name = "website.url.activation", defaultValue = "NO_WEBSITE_ACTIVATION")
    private String websiteUrlActivation;

    @Inject
    private EmailService emailService;

    public void sendCreationMail(UserEntity user) {
        ResponseBodyDto emailBody = new EmailBuilder("/email/create-user.html").addSelector("${LOGIN}", user.username)
                .addSelector(WEBSITE_NAME, websiteName).addSelector(WEBSITE_URL, websiteUrl)
                .addSelector("${WEBSITE_ACTION}", websiteUrlActivation + user.activationToken).build();

        emailService.sendEmailOutgoing(new EmailRequest(user.email, websiteName + " - Activer votre compte", emailBody.content(), user));
    }

    public void sendIdentityTokenMail(UserEntity user) {

        ResponseBodyDto emailBody = new EmailBuilder("/email/identity-token.html").addSelector(WEBSITE_NAME, websiteName)
                .addSelector(WEBSITE_URL, websiteUrl).addSelector("${IDENTITY_TOKEN}", user.identityToken).build();

        emailService.sendEmailOutgoing(
                new EmailRequest(user.email, websiteName + " - Vérification de votre identité", emailBody.content(), user));
    }

    public void sendResetPassword(UserEntity user, String newPassword) {

        ResponseBodyDto emailBody = new EmailBuilder("/email/reset-password.html").addSelector(WEBSITE_NAME, websiteName)
                .addSelector(WEBSITE_URL, websiteUrl).addSelector("${NEW_PASSWORD}", newPassword).build();

        emailService.sendEmailOutgoing(new EmailRequest(user.email, websiteName + " - Nouveau mot de passe", emailBody.content(), user));
    }
}
