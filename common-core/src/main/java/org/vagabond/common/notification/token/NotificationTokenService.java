package org.vagabond.common.notification.token;

import java.time.LocalDateTime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.service.BaseService;

@ApplicationScoped
public class NotificationTokenService extends BaseService<NotificationTokenEntity> {

    @Inject
    NotificationTokenRepository notificationTokenRepository;

    @Override
    public BaseRepository<NotificationTokenEntity> getRepository() {
        return notificationTokenRepository;
    }

    @Transactional
    public NotificationTokenEntity mergeToken(UserEntity user, String token) {
        var entity = notificationTokenRepository.find("WHERE user.id = ?1 and token = ?2", user.id, token).firstResultOptional();
        if (!entity.isPresent()) {
            var newEntity = new NotificationTokenEntity();
            newEntity.user = user;
            newEntity.token = token;
            newEntity.creationDate = LocalDateTime.now();
            newEntity.updatedDate = LocalDateTime.now();
            newEntity.active = true;
            persist(newEntity);
            return newEntity;
        }
        return entity.get();
    }

}