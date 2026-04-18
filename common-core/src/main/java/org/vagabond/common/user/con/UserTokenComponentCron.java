package org.vagabond.common.user.con;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.vagabond.common.user.service.UserTokenService;

import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class UserTokenComponentCron {

    @Inject
    private UserTokenService userTokenService;

    @Scheduled(cron = "{cron.token.cleanup:0 0 2 * * ?}")
    public void cleanupExpiredTokens() {
        long deleted = userTokenService.deleteExpiredTokens();
        Log.infof("%d token(s) expiré(s) supprimé(s)", deleted);
    }
}
