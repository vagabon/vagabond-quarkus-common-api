package org.vagabond.common.news;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.service.BaseService;

@ApplicationScoped
public class NewsService extends BaseService<NewsEntity> {

    @Inject
    NewsRepository newsRepository;

    @Override
    public BaseRepository<NewsEntity> getRepository() {
        return newsRepository;
    }

}