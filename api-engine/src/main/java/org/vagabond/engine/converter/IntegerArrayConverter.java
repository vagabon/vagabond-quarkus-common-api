package org.vagabond.engine.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.vagabond.engine.exeption.TechnicalException;

@Converter
public class IntegerArrayConverter implements AttributeConverter<Integer[], String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Integer[] attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new TechnicalException("Error converting Integer[] to JSON", e);
        }
    }

    @Override
    public Integer[] convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, Integer[].class);
        } catch (JsonProcessingException e) {
            throw new TechnicalException("Error converting JSON to Integer[]", e);
        }
    }
}
