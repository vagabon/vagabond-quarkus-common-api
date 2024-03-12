package org.vagabond.common.api.news;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

import org.vagabond.common.news.NewsEntity;
import org.vagabond.common.news.NewsService;
import org.vagabond.common.news.payload.NewsResponse;
import org.vagabond.engine.crud.dto.PageResponse;
import org.vagabond.engine.crud.resource.BaseUploadResource;
import org.vagabond.engine.mapper.MapperUtils;

import io.smallrye.common.annotation.RunOnVirtualThread;

@Path("/news")
@RunOnVirtualThread
public class NewsResource extends BaseUploadResource<NewsEntity> {

    public static final String UPLOAD_DIRECTORY = "/news";

    @Inject
    NewsService newsService;

    @PostConstruct
    public void postConstruct() {
        service = newsService;
    }

    @Override
    public String getDirectoryName() {
        return UPLOAD_DIRECTORY;
    }

    @Override
    public Object toDto(NewsEntity entity) {
        return MapperUtils.toDto(entity, NewsResponse.class);
    }

    @Override
    public PageResponse toPage(PageResponse response) {
        return MapperUtils.toPage(response, NewsResponse.class);
    }
}