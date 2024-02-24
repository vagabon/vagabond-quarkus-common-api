package org.vagabond.engine.crud.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class QueryUtils {

    private static final String EMPTY = "%";
    private static final String POURCENT = "%";

    public static String getLike(String value) {
        String valueDefined = value == null ? EMPTY : value;
        var valueLike = new StringBuilder();
        valueLike.append(POURCENT);
        valueLike.append(valueDefined.toUpperCase());
        valueLike.append(POURCENT);
        return valueLike.toString();
    }
}
