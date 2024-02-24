package org.vagabond.engine.exeption;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class MetierExceptionTest {
    MetierException exception;

    @Test
    void testInstanciate() {
        exception = new MetierException();
        exception = new MetierException("MESSAGE");
        Assertions.assertEquals("MESSAGE", exception.getMessage());
        exception = new MetierException("MESSAGE", new Throwable());
        Assertions.assertEquals("MESSAGE", exception.getMessage());
    }
}
