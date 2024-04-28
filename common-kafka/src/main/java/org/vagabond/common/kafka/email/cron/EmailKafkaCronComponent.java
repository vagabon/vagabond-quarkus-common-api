package org.vagabond.common.kafka.email.cron;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.apache.commons.lang3.BooleanUtils;
import org.vagabond.common.email.EmailService;
import org.vagabond.common.kafka.engine.ICronComponent;

import io.quarkus.logging.Log;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class EmailKafkaCronComponent implements ICronComponent {

    public static final int MAX_EMAIL = 100;

    @Inject
    EmailService emailService;

    @Inject
    Mailer mailer;

    @Scheduled(cron = "{cron.email:0/30 * * * * ?}")
    public void runCron() {
        var date = LocalDateTime.now().minus(SECONDES_ADD_TO_CHECK, ChronoUnit.SECONDS);
        String hql = "where send = false and error is null and active = true and creationDate <= ?1";

        var emails = emailService.getRepository().find(hql, date).page(0, MAX_EMAIL).list();

        var nbSend = 0;
        for (var emailEntity : emails) {
            var mail = new Mail();
            mail.setTo(Arrays.asList(emailEntity.to));
            mail.setSubject(emailEntity.subject);
            mail.setText(emailEntity.text);
            var isSend = emailService.sendMail(emailEntity);
            if (BooleanUtils.isTrue(isSend)) {
                nbSend++;
            }
        }
        if (nbSend > 0) {
            Log.infof("CRON runCron do send mail %S times", nbSend);
        }
    }
}
