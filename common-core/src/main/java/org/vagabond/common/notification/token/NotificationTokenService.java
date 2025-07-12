package org.vagabond.common.notification.token;

import java.time.LocalDateTime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.service.BaseService;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;

@ApplicationScoped
public class NotificationTokenService extends BaseService<NotificationTokenEntity> {

    @Inject
    NotificationTokenRepository notificationTokenRepository;

    @Override
    public BaseRepository<NotificationTokenEntity> getRepository() {
        return notificationTokenRepository;
    }

    @WithTransaction
    public NotificationTokenEntity mergeToken(UserEntity user, String token) {
        var entity = notificationTokenRepository.find("WHERE user.id = ?1 and token = ?2", user.id, token).firstResult().await()
                .indefinitely();
        if (entity != null) {
            var newEntity = new NotificationTokenEntity();
            newEntity.user = user;
            newEntity.token = token;
            newEntity.creationDate = LocalDateTime.now();
            newEntity.updatedDate = LocalDateTime.now();
            newEntity.active = true;
            return persist(newEntity);
        }
        return entity;
    }

}