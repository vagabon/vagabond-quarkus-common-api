package org.vagabond.common.notification.service;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.vagabond.common.notification.entity.NotificationEntity;
import org.vagabond.common.notification.payload.NotificationRequest;
import org.vagabond.common.notification.repository.NotificationRepository;
import org.vagabond.common.notification.token.NotificationTokenRepository;
import org.vagabond.common.user.entity.UserEntity;
import org.vagabond.engine.crud.response.PageResponse;
import org.vagabond.engine.crud.service.BaseService;

import io.quarkus.panache.common.Page;
import lombok.Getter;

@ApplicationScoped
public class NotificationService extends BaseService<NotificationEntity> {

    public static final int MAX_NOTIFICATIONS = 50;

    @ConfigProperty(name = "website.url")
    private String websiteUrl;

    @Inject
    @Getter
    private NotificationRepository repository;

    @Inject
    private NotificationTokenRepository notificationTokenRepository;

    @Inject
    private NotificationKafkaService notificationKafkaService;

    public PageResponse<NotificationEntity> search(Long userId, String category, String type, Long entityId,
            String search, int page) {
        var query = getRepository().search(userId, category, type, entityId, search);

        query.page(Page.ofSize(MAX_NOTIFICATIONS));
        var content = query.page(Page.of(page, MAX_NOTIFICATIONS)).list();
        return new PageResponse<>(page, query.pageCount(), query.count(), MAX_NOTIFICATIONS, content);
    }

    public Long countNotReadByUser(Long userId) {
        return repository.countNotReadByUser(userId);
    }

    @Transactional
    public NotificationEntity read(Long notificationId) {
        var notification = findById(notificationId);
        notification.read = notification.read == null ? Boolean.TRUE : !notification.read;
        return persist(notification);
    }

    @Transactional
    public int readAll(Long userId) {
        return repository.readAllByUser(userId);
    }

    public void sendNotification(UserEntity userConnected, List<Long> userIds,
            NotificationRequest notification, Long entityId, String superType, String category, String type) {

        var newEntity = new NotificationEntity();
        newEntity.title = notification.title;
        newEntity.message = notification.body;
        newEntity.url = websiteUrl + (StringUtils.isEmpty(notification.url) ? "/profile" : notification.url);
        newEntity.superType = superType;
        newEntity.category = category;
        newEntity.type = type;
        newEntity.entityId = entityId;
        newEntity.user = userConnected;
        String joinUserIds = userIds != null
                ? String.join(",", userIds.stream().map(Object::toString).toArray(String[]::new))
                : null;
        newEntity.users = joinUserIds;
        newEntity.creationDate = LocalDateTime.now();
        newEntity.updatedDate = LocalDateTime.now();
        newEntity.active = true;
        persist(newEntity);

        if (getCountLastSend(category, type, userConnected.id) < 2L || userConnected.profiles.stream()
                .filter(profile -> "ADMIN".equals(profile.name)).count() == 1) {
            notification.tokens = getTokens(userIds);
            notificationKafkaService.registerNotification(notification);
        }
    }

    @Transactional
    public List<String> getTokens(List<Long> userIds) {
        var entityTokens = notificationTokenRepository.find("WHERE user.id in ?1", userIds).list();
        return entityTokens.stream().map(entity -> entity.token).distinct().toList();
    }

    @Transactional
    public Long getCountLastSend(String category, String type, Long userId) {
        var date = LocalDateTime.now().plusMinutes(-5);
        return repository.countCountLastSend(date, category, type, userId);
    }

}