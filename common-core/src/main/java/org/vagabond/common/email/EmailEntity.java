package org.vagabond.common.email;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.vagabond.engine.crud.entity.BaseCrudEntity;

@Entity
@Table(name = "email-queue")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class EmailEntity extends BaseCrudEntity {

    @Column(name = "email")
    public String to;

    @Column(name = "subject")
    public String subject;

    @Column(name = "text", columnDefinition = "TEXT")
    public String text;

    @Column(name = "user_id")
    public Long userId;

    @Column(name = "is_send")
    public Boolean send;

    @Column(name = "is_error", nullable = true)
    public Boolean error;
}
