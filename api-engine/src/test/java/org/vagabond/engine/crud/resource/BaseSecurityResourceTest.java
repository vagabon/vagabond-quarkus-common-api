package org.vagabond.engine.crud.resource;

import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.vagabond.engine.crud.entity.BaseCrudEntity;
import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.service.BaseService;
import org.vagabond.engine.exeption.MetierException;
import org.vagabond.engine.gateway.user.GatewayUser;
import org.vagabond.engine.user.CurrentUser;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import lombok.Getter;

class DummyEntity extends BaseCrudEntity {
}

@ApplicationScoped
class DummyRepository extends BaseRepository<DummyEntity> {
}

@ApplicationScoped
class DummyService extends BaseService<DummyEntity> {

    @Inject
    @Getter
    DummyRepository repository;

}

@Path("/test-security-resource")
class DummySecurityResource
    extends BaseSecurityResource<DummyEntity> {

    public DummySecurityResource(DummyService service) {
        this.service = service;
    }
}

@QuarkusTest
class BaseSecurityResourceTest {

    @InjectMock
    GatewayUser gatewayUser;

    @InjectMock
    DummyService crudService;

    @Inject
    DummySecurityResource dummyResource;

    @Test
    void testFindById_NotFound() {
        when(gatewayUser.get()).thenReturn(null);
        when(crudService.findById(99L)).thenReturn(null);

        given()
            .when().get("/test-security-resource/99")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void test_verifyUserConnected() {
        var user = new CurrentUser(1L, "user", "user@gmail.com",
            Set.of("USER"));

        assertThrows(MetierException.class,
            () -> dummyResource.verifyUserConnected(user, 99L));
    }

    @Test
    void test_findById_when_not_active_should_throwMetierException() {
        var entity = new DummyEntity();
        entity.active = false;
        when(crudService.findById(99L)).thenReturn(entity);

        var mockUser = new CurrentUser(1L, "user", "user@gmail.com", Set.of("USER"));
        when(gatewayUser.get()).thenReturn(mockUser);

        assertThrows(MetierException.class,
            () -> dummyResource.findById(99L));
    }
}