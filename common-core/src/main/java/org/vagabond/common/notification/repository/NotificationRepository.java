package org.vagabond.common.notification.repository;

import java.time.LocalDateTime;

import jakarta.enterprise.context.ApplicationScoped;

import org.vagabond.common.notification.entity.NotificationEntity;
import org.vagabond.engine.crud.repository.BaseRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

@ApplicationScoped
public class NotificationRepository extends BaseRepository<NotificationEntity> {

    public static final String LIKE_FORMAT = "%%%s%%";

    public PanacheQuery<NotificationEntity> search(Long userId, String category, String type, Long entityId,
            String search) {
        var categoryLike = String.format(LIKE_FORMAT, category.toLowerCase());
        var typeLike = String.format(LIKE_FORMAT, type.toLowerCase());
        var searchLike = String.format(LIKE_FORMAT, search.toLowerCase());

        var sql = """
                    WHERE users = ?1
                        AND lower(category) like ?2
                        AND lower(type) like ?3
                        AND (lower(title) like ?4 OR lower(message) like ?4)
                """;
        var order = " order by id desc";

        if (entityId != null) {
            sql = String.format("%s AND entityId = ?5", sql);
            return find(String.format("%s %s", sql, order), userId, categoryLike, typeLike, searchLike,
                    entityId);
        }

        return find(String.format("%s %s", sql, order), userId, categoryLike, typeLike, searchLike);
    }

    public long countNotReadByUser(Long userId) {
        return count("where active = true and (read is null or read = false) and user.id = ?1", userId);
    }

    public long countCountLastSend(LocalDateTime date, String category, String type, Long userId) {
        return count("where creationDate > ?1 and category = ?2 and type = ?3 and user.id = ?4", date,
                category, type, userId);
    }

    public int readAllByUser(Long userId) {
        return update("read = ?1 where active = true and (read is null or read = false) and user.id = ?2",
                true, userId);
    }
}
