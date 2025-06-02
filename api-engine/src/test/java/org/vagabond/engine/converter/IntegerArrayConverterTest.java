package org.vagabond.engine.converter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.vagabond.engine.exeption.TechnicalException;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class IntegerArrayConverterTest {

    IntegerArrayConverter converter = new IntegerArrayConverter();

    @Test
    void testConvertToDatabaseColumn() {
        var value = converter.convertToDatabaseColumn(null);
        Assertions.assertEquals("null", value);

        var tab = new Integer[1];
        tab[0] = 1;
        value = converter.convertToDatabaseColumn(tab);
        Assertions.assertEquals("[1]", value);
    }

    @Test
    void testConvertToEntityAttribute() {
        var value = converter.convertToEntityAttribute("[1]");
        Assertions.assertEquals(1, value[0]);
    }

    @Test
    void testConvertToEntityAttributeException() {
        assertThrows(TechnicalException.class, () -> converter.convertToEntityAttribute("a"));
    }
}
