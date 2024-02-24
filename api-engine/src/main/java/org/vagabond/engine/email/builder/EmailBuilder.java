package org.vagabond.engine.email.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.vagabond.engine.email.dto.ResponseBodyDto;
import org.vagabond.engine.email.dto.TempateSelectorDto;
import org.vagabond.engine.email.utils.EmailUtils;
import org.vagabond.engine.exeption.TechnicalException;

import io.smallrye.jwt.util.ResourceUtils;

public class EmailBuilder {

    public static final String ERROR_TEMPLATE_PATH_IS_EMPTY = "Template empty : Provided template path was empty";
    public static final String ERROR_TEMPLATE_EMPTY_FOR_PATH = "Template empty : Got template searching error for path : %s";
    public static final String ERROR_SELECTOR_NOT_FOUND = "No such placeholder %s in template";

    private String template;
    private List<TempateSelectorDto> templateSelectors = new ArrayList<>();
    private List<String> errors = new ArrayList<>();

    public EmailBuilder(String templatePath) {
        errors = new ArrayList<>();
        template = "";
        if (StringUtils.isBlank(templatePath)) {
            errors.add(ERROR_TEMPLATE_PATH_IS_EMPTY);
        } else {
            try {
                template = ResourceUtils.readResource(templatePath);
            } catch (TechnicalException | IOException e) {
                errors.add(String.format(ERROR_TEMPLATE_EMPTY_FOR_PATH, templatePath));
            }
        }

    }

    public EmailBuilder addSelector(String placeholdder, String value) {
        TempateSelectorDto newSelector = new TempateSelectorDto(placeholdder, value);

        List<String> emailErrors = EmailUtils.check(newSelector);
        errors.addAll(emailErrors);

        if (template.contains(placeholdder)) {
            templateSelectors.add(newSelector);
        } else {
            errors.add(String.format(ERROR_SELECTOR_NOT_FOUND, placeholdder));
        }
        return this;

    }

    public ResponseBodyDto build() {
        for (TempateSelectorDto selector : templateSelectors) {
            template = template.replace(selector.placeholder(), selector.value());
        }
        return new ResponseBodyDto(template, errors);
    }

}
