package org.vagabond.common.api.news;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import org.vagabond.common.news.NewsEntity;
import org.vagabond.common.news.NewsService;
import org.vagabond.common.news.payload.NewsResponse;
import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.auth.annotation.AuthRole;
import org.vagabond.engine.auth.annotation.AuthSecure;
import org.vagabond.engine.crud.resource.BaseCrudResource;

import io.smallrye.common.annotation.RunOnVirtualThread;

@Path("/news")
@RunOnVirtualThread
public class NewsResource extends BaseCrudResource<NewsEntity, UserEntity> {

    @Inject
    NewsService newsService;

    @PostConstruct
    public void postConstruct() {
        service = newsService;
        responseClass = NewsResponse.class;
    }

    @AuthSecure
    @AuthRole("ADMIN")
    @Override
    public Response findAll(@QueryParam("page") Integer page, @QueryParam("max") Integer max, @QueryParam("sort") String sort) {
        return super.findAll(page, max, sort);
    }
}