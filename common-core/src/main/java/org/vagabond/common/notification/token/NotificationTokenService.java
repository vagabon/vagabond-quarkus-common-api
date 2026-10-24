package org.vagabond.common.notification.token;

import java.time.Instant;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.service.BaseService;
import org.vagabond.engine.gateway.CurrentUser;

@ApplicationScoped
public class NotificationTokenService extends BaseService<NotificationTokenEntity> {

    @Inject
    NotificationTokenRepository notificationTokenRepository;

    @Override
    public BaseRepository<NotificationTokenEntity> getRepository() {
        return notificationTokenRepository;
    }

    @Transactional
    public NotificationTokenEntity mergeToken(CurrentUser user, String token) {
        var entity = notificationTokenRepository
            .find("WHERE userId = ?1 and token = ?2", user.id(), token)
            .firstResultOptional();
        if (!entity.isPresent()) {
            var newEntity = new NotificationTokenEntity();
            newEntity.userId = user.id();
            newEntity.token = token;
            newEntity.creationDate = Instant.now();
            newEntity.updatedDate = Instant.now();
            newEntity.active = true;
            return persist(newEntity);
        }
        return entity.get();
    }

}