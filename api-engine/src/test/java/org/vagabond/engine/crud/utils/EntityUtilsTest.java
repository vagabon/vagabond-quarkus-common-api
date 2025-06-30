package org.vagabond.engine.crud.utils;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.vagabond.engine.crud.entity.BaseEntity;

import io.quarkus.test.junit.QuarkusTest;
import lombok.Getter;
import lombok.Setter;

@QuarkusTest
class EntityUtilsTest {

    @Test
    void test() {
        var now = LocalDateTime.now();

        Entity entity1 = new Entity();
        entity1.name = "name";
        entity1.username = "username";

        Entity entity2 = new Entity();
        entity2.name = "name2";
        entity2.username = "username";
        entity2.myDate = now;

        EntityUtils.setEntity(entity1, entity2, true);

        Assertions.assertEquals("name2", entity1.name);
        Assertions.assertEquals("username", entity1.username);
        Assertions.assertEquals(now, entity1.myDate);

        entity2.myDate = null;
        EntityUtils.setEntity(entity1, entity2, true);
        Assertions.assertEquals(null, entity1.myDate);
    }

    @Test
    void Given_EntityUtilsTest_When_isObjectDiff_Then_return_correcValue() {
        Assertions.assertEquals(false, EntityUtils.isObjectDiff(null, null));
        Assertions.assertEquals(true, EntityUtils.isObjectDiff(null, "test"));
        Assertions.assertEquals(false, EntityUtils.isObjectDiff("test", "test"));
        Assertions.assertEquals(true, EntityUtils.isObjectDiff("test", "test2"));
    }

    @Getter
    @Setter
    class Entity extends BaseEntity {
        public String name;
        public String username;
        public LocalDateTime myDate;
    }
}
