package org.vagabond.engine.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.vagabond.engine.exeption.TechnicalException;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class StringArrayConverterTest {
    StringArrayConverter converter = new StringArrayConverter();

    @Test
    void testConvertToDatabaseColumn() {
        var value = converter.convertToDatabaseColumn(null);
        Assertions.assertEquals("null", value);

        var tab = new String[1];
        tab[0] = "1";
        value = converter.convertToDatabaseColumn(tab);
        Assertions.assertEquals("[\"1\"]", value);

    }

    @Test
    void testConvertToDatabaseColumnException() throws JsonProcessingException {
        var input = new String[] { "test" };
        converter.setObjectMapper(Mockito.mock(ObjectMapper.class));
        Mockito.when(converter.getObjectMapper().writeValueAsString(input)).thenThrow(new JsonProcessingException("Mock error") {
        });
        assertThrows(TechnicalException.class, () -> converter.convertToDatabaseColumn(input));
    }

    @Test
    void testConvertToEntityAttribute() {
        var value = converter.convertToEntityAttribute("[1]");
        Assertions.assertEquals("1", value[0]);
    }

    @Test
    void testConvertToEntityAttributeException() {
        assertThrows(TechnicalException.class, () -> converter.convertToEntityAttribute("~~"));
    }
}
