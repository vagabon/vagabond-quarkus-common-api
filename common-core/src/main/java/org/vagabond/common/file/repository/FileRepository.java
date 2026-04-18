package org.vagabond.common.file.repository;

import jakarta.enterprise.context.ApplicationScoped;

import org.vagabond.common.file.entity.FileEntity;
import org.vagabond.engine.crud.repository.BaseRepository;

@ApplicationScoped
public class FileRepository extends BaseRepository<FileEntity> {

}
