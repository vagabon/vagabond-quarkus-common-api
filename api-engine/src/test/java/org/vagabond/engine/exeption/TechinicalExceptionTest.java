package org.vagabond.engine.exeption;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class TechnicalExceptionTest {
    TechnicalException exception;

    @Test
    void testInstanciate() {
        exception = new TechnicalException();
        exception = new TechnicalException("MESSAGE", new Throwable());
        Assertions.assertEquals("MESSAGE", exception.getMessage());
    }
}
