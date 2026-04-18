package org.vagabond.common.email.repository;

import jakarta.enterprise.context.ApplicationScoped;

import org.vagabond.common.email.entity.EmailEntity;
import org.vagabond.engine.crud.repository.BaseRepository;

@ApplicationScoped
public class EmailRepository extends BaseRepository<EmailEntity> {

}
