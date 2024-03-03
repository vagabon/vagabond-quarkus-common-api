package org.vagabond.common.auth.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.email.builder.EmailBuilder;
import org.vagabond.engine.email.dto.ResponseBodyDto;
import org.vagabond.engine.email.kafka.payload.EmailKafkaRequest;
import org.vagabond.engine.email.kafka.service.EmailKafkaService;

@ApplicationScoped
public class AuthEmailService {

    private static final String WEBSITE_NAME = "${WEBSITE_NAME}";
    private static final String WEBSITE_URL = "${WEBSITE_URL}";

    @ConfigProperty(name = "website.name")
    private String websiteName;
    @ConfigProperty(name = "website.url")
    private String websiteUrl;
    @ConfigProperty(name = "website.url.activation")
    private String websiteUrlActivation;

    @Inject
    private EmailKafkaService emailKafkaService;

    public void sendCreationMail(UserEntity user) {
        ResponseBodyDto emailBody = new EmailBuilder("/email/create-user.html").addSelector("${LOGIN}", user.username)
                .addSelector(WEBSITE_NAME, websiteName).addSelector(WEBSITE_URL, websiteUrl)
                .addSelector("${WEBSITE_ACTION}", websiteUrlActivation + user.activationToken).build();

        emailKafkaService
                .sendEmailOutgoing(new EmailKafkaRequest(user.email, websiteName + " - Activer votre compte", emailBody.content()));
    }

    public void sendIdentityTokenMail(UserEntity user) {

        ResponseBodyDto emailBody = new EmailBuilder("/email/identity-token.html").addSelector(WEBSITE_NAME, websiteName)
                .addSelector(WEBSITE_URL, websiteUrl).addSelector("${IDENTITY_TOKEN}", user.identityToken).build();

        emailKafkaService.sendEmailOutgoing(
                new EmailKafkaRequest(user.email, websiteName + " - Vérification de votre identité", emailBody.content()));
    }

    public void sendResetPassword(UserEntity user, String newPassword) {

        ResponseBodyDto emailBody = new EmailBuilder("/email/reset-password.html").addSelector(WEBSITE_NAME, websiteName)
                .addSelector(WEBSITE_URL, websiteUrl).addSelector("${NEW_PASSWORD}", newPassword).build();

        emailKafkaService
                .sendEmailOutgoing(new EmailKafkaRequest(user.email, websiteName + " - Nouveau mot de passe", emailBody.content()));
    }
}
