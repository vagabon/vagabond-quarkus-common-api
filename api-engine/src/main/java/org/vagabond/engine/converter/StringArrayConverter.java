package org.vagabond.engine.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.vagabond.engine.exeption.TechnicalException;

@Converter
public class StringArrayConverter implements AttributeConverter<String[], String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(String[] attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new TechnicalException("Error converting String[] to JSON", e);
        }
    }

    @Override
    public String[] convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, String[].class);
        } catch (JsonProcessingException e) {
            throw new TechnicalException("Error converting JSON to String[]", e);
        }
    }
}
