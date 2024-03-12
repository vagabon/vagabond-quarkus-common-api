package org.vagabond.common.email.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.vagabond.engine.email.dto.ResponseBodyDto;
import org.vagabond.engine.email.dto.TempateSelectorDto;
import org.vagabond.engine.email.utils.EmailUtils;

@QuarkusTest
class EmailUtilsTest {

    @Test
    void given_validAttributes_when_newTempateSelectorDto_then_returnNoErrors() {
        String validValue = "value";
        String validPlaceholder = "placeholdder";
        TempateSelectorDto selector = new TempateSelectorDto(validPlaceholder, validValue);

        List<String> errors = EmailUtils.check(selector);

        assertTrue(errors.isEmpty());
    }

    @Test
    void given_invalidValue_when_newTempateSelectorDto_then_returnExpectedError() {
        String invalidValue = "  ";
        String validPlaceholder = "placeholdder";
        String expectedError = "value must not be blank";
        TempateSelectorDto selector = new TempateSelectorDto(validPlaceholder, invalidValue);

        List<String> errors = EmailUtils.check(selector);

        Assertions.assertEquals(expectedError, errors.get(0));
    }

    @Test
    void given_invalidPlaceholder_when_newTempateSelectorDto_then_returnExpectedError() {
        String validValue = "value";
        String invalidPlaceholder = "";
        String expectedError = "placeholder must not be blank";
        TempateSelectorDto selector = new TempateSelectorDto(invalidPlaceholder, validValue);

        List<String> errors = EmailUtils.check(selector);

        Assertions.assertEquals(expectedError, errors.get(0));
    }

    @Test
    void given_contentIsBlank_when_newResponseBodyDto_then_returnExpectedError() {
        String expectedError = "content must not be blank";
        ResponseBodyDto selector = new ResponseBodyDto("", new ArrayList<>());

        List<String> errors = EmailUtils.check(selector);

        Assertions.assertEquals(expectedError, errors.get(0));
    }
}
