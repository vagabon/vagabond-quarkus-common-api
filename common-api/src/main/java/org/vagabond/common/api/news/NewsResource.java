package org.vagabond.common.api.news;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

import org.vagabond.common.news.NewsEntity;
import org.vagabond.common.news.NewsService;
import org.vagabond.common.news.payload.NewsResponse;
import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.crud.resource.BaseUploadResource;

import io.smallrye.common.annotation.RunOnVirtualThread;

@Path("/news")
@RunOnVirtualThread
public class NewsResource extends BaseUploadResource<NewsEntity, UserEntity> {

    public static final String UPLOAD_DIRECTORY = "/news";

    @Inject
    NewsService newsService;

    @PostConstruct
    public void postConstruct() {
        service = newsService;
        responseClass = NewsResponse.class;
    }

    @Override
    public String getDirectoryName() {
        return UPLOAD_DIRECTORY;
    }
}