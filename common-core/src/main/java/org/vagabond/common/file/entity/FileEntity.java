package org.vagabond.common.file.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.vagabond.engine.crud.entity.BaseCrudEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "file", schema = "public")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class FileEntity extends BaseCrudEntity {

    public String name;
    public String directory;
    public String path;

    public Long userId;
}
