package org.vagabond.common.api.news;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import org.vagabond.common.news.entity.NewsEntity;
import org.vagabond.common.news.payload.NewsResponse;
import org.vagabond.common.news.service.NewsService;
import org.vagabond.engine.auth.decorator.AuthRole;
import org.vagabond.engine.crud.resource.BaseCrudResource;

import io.smallrye.common.annotation.RunOnVirtualThread;

@Path("/news")
@RunOnVirtualThread
public class NewsResource extends BaseCrudResource<NewsEntity> {

    @Inject
    NewsService newsService;

    @PostConstruct
    public void postConstruct() {
        service = newsService;
        responseClass = NewsResponse.class;
    }

    @Override
    @AuthRole()
    public Response findBy(
        @QueryParam("fields") String fields,
        @QueryParam("values") String values,
        @QueryParam("first") Integer first,
        @QueryParam("max") Integer max) {
        return super.findBy(fields, values, first, max);
    }
}