package org.vagabond.common.api.news;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.vagabond.common.news.NewsEntity;
import org.vagabond.common.news.NewsService;
import org.vagabond.common.news.payload.NewsResponse;
import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.crud.resource.BaseCrudResource;

import io.smallrye.common.annotation.RunOnVirtualThread;

@Path("/news")
@SecurityRequirement(name = "SecurityScheme")
@RunOnVirtualThread
public class NewsResource extends BaseCrudResource<NewsEntity, UserEntity> {

    @Inject
    NewsService newsService;

    @PostConstruct
    public void postConstruct() {
        service = newsService;
        responseClass = NewsResponse.class;
    }
}