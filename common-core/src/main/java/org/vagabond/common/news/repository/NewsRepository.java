package org.vagabond.common.news.repository;

import jakarta.enterprise.context.ApplicationScoped;

import org.vagabond.common.news.entity.NewsEntity;
import org.vagabond.engine.crud.repository.BaseRepository;

@ApplicationScoped
public class NewsRepository extends BaseRepository<NewsEntity> {

}
