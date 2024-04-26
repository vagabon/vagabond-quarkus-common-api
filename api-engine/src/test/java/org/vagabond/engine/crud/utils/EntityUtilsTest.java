package org.vagabond.engine.crud.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.vagabond.engine.crud.entity.BaseEntity;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class EntityUtilsTest {

    @Test
    void test() {

        Entity entity1 = new Entity();
        entity1.name = "name";
        entity1.username = "username";

        Entity entity2 = new Entity();
        entity2.name = "name2";

        EntityUtils.setEntity(entity1, entity2);

        Assertions.assertEquals("name2", entity1.name);
        Assertions.assertEquals("username", entity1.username);
    }

    @Test
    void Given_EntityUtilsTest_When_isObjectDiff_Then_return_correcValue() {
        Assertions.assertEquals(false, EntityUtils.isObjectDiff(null, null));
        Assertions.assertEquals(true, EntityUtils.isObjectDiff(null, "test"));
        Assertions.assertEquals(false, EntityUtils.isObjectDiff("test", "test"));
        Assertions.assertEquals(true, EntityUtils.isObjectDiff("test", "test2"));
    }

    class Entity extends BaseEntity {
        public String name;
        public String username;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
