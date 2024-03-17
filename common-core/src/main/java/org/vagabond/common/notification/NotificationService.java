package org.vagabond.common.notification;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.vagabond.common.notification.kafka.NotificationKafkaService;
import org.vagabond.common.notification.payload.NotificationRequest;
import org.vagabond.common.notification.token.NotificationTokenRepository;
import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.service.BaseService;

@ApplicationScoped
public class NotificationService extends BaseService<NotificationEntity> {
    @ConfigProperty(name = "website.url")
    private String websiteUrl;

    @Inject
    private NotificationRepository notificationRepository;

    @Inject
    private NotificationTokenRepository notificationTokenRepository;

    @Inject
    private NotificationKafkaService notificationKafkaService;

    @Override
    public BaseRepository<NotificationEntity> getRepository() {
        return notificationRepository;
    }

    public Long countByMemberOrCreator(Long userId) {
        return notificationRepository.count("where active = true and read = false and user.id = ?1", userId);
    }

    @Transactional
    public int readAll(Long userId) {
        return notificationRepository.update("read = ?1 where active = true and read = false and user.id = ?2", true, userId);
    }

    public void sendNotification(UserEntity userConnected, List<Long> userIds, NotificationRequest notification, Long entityId,
            String superType, String category, String type) {

        var newEntity = new NotificationEntity();
        newEntity.title = notification.title;
        newEntity.message = notification.body;
        newEntity.url = websiteUrl + notification.url;
        newEntity.superType = superType;
        newEntity.category = category;
        newEntity.type = type;
        newEntity.entityId = entityId;
        newEntity.user = userConnected;
        String joinUserIds = userIds != null ? String.join(",", userIds.stream().map(Object::toString).toArray(String[]::new)) : null;
        newEntity.users = joinUserIds;
        newEntity.creationDate = LocalDateTime.now();
        newEntity.updatedDate = LocalDateTime.now();
        newEntity.active = true;
        persist(newEntity);

        if (getCountLastSend(entityId, userConnected.id) < 2L) {
            notification.tokens = getTokens(userIds);
            notificationKafkaService.registerNotification(notification);
        }
    }

    @Transactional
    public List<String> getTokens(List<Long> userIds) {
        var entityTokens = notificationTokenRepository.find("WHERE user.id in ?1", userIds).list();
        return entityTokens.stream().map(entity -> entity.token).toList();
    }

    @Transactional
    public Long getCountLastSend(Long entityId, Long userId) {
        var date = LocalDateTime.now().plusMinutes(-5);
        return notificationRepository.count("where creationDate > ?1 and entityId = ?2 and user.id = ?3", date, entityId, userId);
    }

}