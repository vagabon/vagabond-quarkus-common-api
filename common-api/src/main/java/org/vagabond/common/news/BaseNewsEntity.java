package org.vagabond.common.news;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.crud.entity.BaseCrudEntity;

@MappedSuperclass
@Table(schema = "public")
@EqualsAndHashCode(callSuper = true)
public abstract class BaseNewsEntity extends BaseCrudEntity {

    public String title;
    public String avatar;
    public String image;
    public String resume;
    public String description;
    public String tags;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    public UserEntity user;
}
