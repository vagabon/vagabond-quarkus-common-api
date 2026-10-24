package org.vagabond.common.news.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

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

    public Long userId;
}
