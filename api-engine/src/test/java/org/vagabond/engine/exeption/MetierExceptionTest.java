package org.vagabond.engine.exeption;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class MetierExceptionTest {
    MetierException exception;

    @Test
    void testInstanciate() {
        exception = new MetierException();
        exception = new MetierException("MESSAGE");
        assertEquals("MESSAGE", exception.getMessage());

        exception = new MetierException("MESSAGE", new Throwable());
        assertEquals("MESSAGE", exception.getMessage());
    }
}
