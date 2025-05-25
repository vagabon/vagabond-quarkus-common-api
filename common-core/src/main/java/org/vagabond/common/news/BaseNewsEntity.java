package org.vagabond.common.news;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.crud.entity.BaseCrudEntity;

import lombok.EqualsAndHashCode;

@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class BaseNewsEntity extends BaseCrudEntity {

    public String title;
    public String avatar;
    public String image;

    @Column(columnDefinition = "TEXT")
    public String resume;

    @Column(columnDefinition = "TEXT")
    public String description;

    public String tags;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    public UserEntity user;
}
