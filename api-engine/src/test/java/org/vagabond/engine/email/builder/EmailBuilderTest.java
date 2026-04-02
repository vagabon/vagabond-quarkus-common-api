package org.vagabond.engine.email.builder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class EmailBuilderTest {

    @Test
    void testBuild() {
        var build = new EmailBuilder("/no-template").addSelector("test", null).build();

        Assertions.assertEquals(3, build.errors().size());
    }
}
