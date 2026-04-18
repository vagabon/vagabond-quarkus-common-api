package org.vagabond.common.notification.repository;

import jakarta.enterprise.context.ApplicationScoped;

import org.vagabond.common.notification.entity.NotificationEntity;
import org.vagabond.engine.crud.repository.BaseRepository;

@ApplicationScoped
public class NotificationRepository extends BaseRepository<NotificationEntity> {

}
