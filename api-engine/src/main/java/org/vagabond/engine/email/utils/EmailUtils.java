package org.vagabond.engine.email.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.quarkus.logging.Log;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class EmailUtils {

    private static final Validator contentChecker = Validation.buildDefaultValidatorFactory().getValidator();

    private EmailUtils() {
    }

    public static List<String> check(Record builderDto) {

        final Set<ConstraintViolation<Record>> violations = new HashSet<>();
        final List<String> errorMessages = new ArrayList<>();
        String currentErrorMessage;

        violations.addAll(contentChecker.validate(builderDto));

        for (var currentViolation : violations) {
            currentErrorMessage = currentViolation.getPropertyPath() + " " + currentViolation.getMessage();
            errorMessages.add(currentErrorMessage);
            Log.error(currentErrorMessage);
        }

        return errorMessages;

    }
}
