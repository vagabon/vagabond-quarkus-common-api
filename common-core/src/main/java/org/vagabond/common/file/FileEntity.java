package org.vagabond.common.file;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.crud.entity.BaseCrudEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "file", schema = "public")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class FileEntity extends BaseCrudEntity {

    public String name;
    public String directory;
    public String path;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    public UserEntity user;
}
