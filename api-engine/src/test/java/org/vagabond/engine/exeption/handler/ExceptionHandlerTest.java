package org.vagabond.engine.exeption.handler;

import java.net.URI;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.vagabond.engine.crud.resource.BaseSecurityResource;
import org.vagabond.engine.exeption.MetierException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;

@ExtendWith(MockitoExtension.class)
@MockitoSettings()
@QuarkusTest
class ExceptionHandlerTest {

    @Mock
    UriInfo uriInfo;

    @InjectMocks
    ExceptionHandler handler;

    MockedStatic<Log> logMock;

    @BeforeEach
    @SuppressWarnings("null")
    void setUp() {
        logMock = mockStatic(Log.class);
        when(uriInfo.getRequestUri())
            .thenReturn(URI.create("http://localhost/api/movies/1"));
    }

    @AfterEach
    void tearDown() {
        logMock.close();
    }

    @Test
    void clientWebApplicationException404_returnsNotFoundWithoutEntity() {
        var clientResponse = mock(Response.class);
        when(clientResponse.getStatus()).thenReturn(404);
        var ex = mock(ClientWebApplicationException.class);
        when(ex.getResponse()).thenReturn(clientResponse);
        when(ex.getMessage()).thenReturn("not found");

        var response = handler.toResponse(ex);

        assertEquals(404, response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    void clientWebApplicationExceptionNon404_fallsBackToBadRequest() {
        var clientResponse = mock(Response.class);
        when(clientResponse.getStatus()).thenReturn(500);
        var ex = mock(ClientWebApplicationException.class);
        when(ex.getResponse()).thenReturn(clientResponse);
        when(ex.getMessage()).thenReturn("boom");

        var response = handler.toResponse(ex);

        assertEquals(400, response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    void metierException_stackTraceReducedToFirstVagabondFrame() {
        var ex = new MetierException("ERRORS.FOO");

        handler.toResponse(ex);

        assertEquals(1, ex.getStackTrace().length);
        assertTrue(ex.getStackTrace()[0].getClassName().contains("org.vagabond"));
    }

    @Test
    void userNotConnected_returns401() {
        var response = handler.toResponse(
            new MetierException(BaseSecurityResource.USER_NOT_CONNECTED));

        assertEquals(401, response.getStatus());
    }

    @Test
    void notAllowed_returns403() {
        var response = handler.toResponse(new MetierException("ERRORS.NOT_ALLOWED"));

        assertEquals(403, response.getStatus());
    }

    @Test
    void nullUriInfo_doesNotThrow() {
        when(uriInfo.getRequestUri()).thenReturn(null);

        var response = handler.toResponse(new NotFoundException("x"));

        assertEquals(404, response.getStatus());
    }
}