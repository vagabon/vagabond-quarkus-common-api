package org.vagabond.engine.crud.service.query;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public interface IQueryUtils {

    String SELECT = "SELECT";
    String FROM = "FROM";
    String WHERE = "WHERE";
    String AND = "AND";
    String OR = "OR";
    String AND_PATERN = "And";
    String LIKE = "like";
    String TRUE = "true";
    String FALSE = "false";

    String CONDITION_SUPERIOR = ">";
    String CONDITION_SUPERIOR_EGAL = ">=";
    String CONDITION_INFERIOR = "<";
    String CONDITION_INFERIOR_EGAL = "<=";
    String CONDITION_DIFFERENT = "!=";
    String CONDITION_LIKE = "%";
    String CONDITION_IS = "~";
    String CONDITION_EGAL = "=";
    String CONDITION_BRACKET_OPEN = "(";
    String CONDITION_DOUBLE_BRACKET_OPEN = "((";
    String CONDITION_BRACKET_CLOSE = ")";
    String CONDITION_DOUBLE_BRACKET_CLOSE = "))";
    String CONDITATION_OR = "|";
    String FORCE_UPPER = "#";

    default List<QueryUtilsDto> initQueryDto(StringBuilder endQuery, String fields, Object... values) {
        var queryDtos = new LinkedList<QueryUtilsDto>();
        var fieldsDecompose = fields;
        // group by
        var split = fieldsDecompose.split("<<");
        if (split.length > 1) {
            fieldsDecompose = split[0];
            endQuery.append(" group by ").append("" + split[1]);
        }
        // order by
        split = fieldsDecompose.split(">>");
        if (split.length > 1) {
            fieldsDecompose = split[0];
            var order = split[1].endsWith("Desc") ? split[1].replace("Desc", " desc") : split[1];
            endQuery.append(" order by ").append("" + order);
        }
        // create QueryDtos
        var tokens = Arrays.asList(fieldsDecompose.split(AND_PATERN));
        int[] index = { 0 };
        tokens.forEach(token -> {
            if (StringUtils.isNotBlank(token) && values.length > index[0]) {
                queryDtos.add(initQueryDto(index, token, values));
            }
            index[0]++;
        });
        return queryDtos;
    }

    private QueryUtilsDto initQueryDto(int[] index, String token, Object... values) {
        var query = new QueryUtilsDto();
        query.indice = (index[0]);
        query.field = (getFieldName(token));
        query.condition = (getFieldCondition(token));
        query.like = (isLike(token));
        query.bracketOpen = (isBracketOpen(token));
        query.doubleBracketOpen = (isDoubleBracketOpen(token));
        query.bracketClose = (isBracketClose(token));
        query.doubleBracketClose = (isDoubleBracketClose(token));
        query.or = (isOr(token));
        var newValue = values[index[0]];
        if (token.endsWith(FORCE_UPPER)) {
            query.upper = (true);
        } else if (NumberUtils.isCreatable(newValue.toString())) {
            try {
                newValue = Long.parseLong(newValue.toString());
            } catch (NumberFormatException e) {
                newValue = Float.parseFloat(newValue.toString());
            }
        } else if (TRUE.equals(newValue) || (FALSE).equals(newValue)) {
            newValue = TRUE.equals(newValue);
        } else if (!NumberUtils.isParsable(newValue.toString())) {
            query.upper = (true);
        }
        query.value = (newValue);
        return query;
    }

    default String getSqlQuery(List<QueryUtilsDto> queryDtos, boolean actived, StringBuilder endQuery) {
        var requete = new StringBuilder();
        queryDtos.forEach(queryDto -> constructRequest(requete, queryDto));
        if (actived) {
            requete.append(" and e.active = true");
        }
        requete.append(endQuery.toString());
        return requete.toString();
    }

    private void constructRequest(StringBuilder requete, QueryUtilsDto queryDto) {
        var andOr = BooleanUtils.isTrue(queryDto.or) ? OR : AND;
        requete.append(" ").append(requete.toString().contains(WHERE) ? andOr : WHERE);
        if (BooleanUtils.isTrue(queryDto.doubleBracketOpen)) {
            requete.append(" ").append(CONDITION_DOUBLE_BRACKET_OPEN);
        } else if (BooleanUtils.isTrue(queryDto.bracketOpen)) {
            requete.append(" ").append(CONDITION_BRACKET_OPEN);
        }
        if (CONDITION_IS.equals(queryDto.condition)) {
            requete.append(" ").append(queryDto.field).append(" is null ");
        } else if (BooleanUtils.isTrue(queryDto.like)) {
            requete.append(" UPPER(").append(queryDto.field).append(") ").append(queryDto.condition).append(" UPPER(?")
                    .append(queryDto.indice + 1).append(")");
        } else if (BooleanUtils.isTrue(queryDto.upper)) {
            requete.append(" UPPER(").append(queryDto.field).append(") ").append(queryDto.condition).append(" UPPER(?")
                    .append(queryDto.indice + 1).append(")");
        } else {
            requete.append(" ").append(queryDto.field).append(" ").append(queryDto.condition).append(" ?").append(queryDto.indice + 1);
        }
        if (BooleanUtils.isTrue(queryDto.doubleBracketClose)) {
            requete.append(" ").append(CONDITION_DOUBLE_BRACKET_CLOSE);
        } else if (BooleanUtils.isTrue(queryDto.bracketClose)) {
            requete.append(" ").append(CONDITION_BRACKET_CLOSE);
        }
    }

    default String getFieldName(String fieldName) {
        return StringUtils.uncapitalize(fieldName.replace(CONDITION_SUPERIOR_EGAL, "").replace(CONDITION_INFERIOR_EGAL, "")
                .replace(CONDITION_SUPERIOR, "").replace(CONDITION_INFERIOR, "").replace(CONDITION_DIFFERENT, "")
                .replace(CONDITION_LIKE, "").replace(CONDITION_IS, "").replace(CONDITION_EGAL, "").replace(CONDITION_BRACKET_OPEN, "")
                .replace(CONDITION_BRACKET_CLOSE, "").replace(CONDITION_DOUBLE_BRACKET_OPEN, "").replace(CONDITION_DOUBLE_BRACKET_CLOSE, "")
                .replace(CONDITATION_OR, "").replace(" asc", "").replace(" desc", "").replace(FORCE_UPPER, ""));
    }

    default String getFieldCondition(String fieldName) {
        if (fieldName.contains(CONDITION_SUPERIOR_EGAL)) {
            return CONDITION_SUPERIOR_EGAL;
        } else if (fieldName.contains(CONDITION_INFERIOR_EGAL)) {
            return CONDITION_INFERIOR_EGAL;
        } else if (fieldName.contains(CONDITION_SUPERIOR)) {
            return CONDITION_SUPERIOR;
        } else if (fieldName.contains(CONDITION_INFERIOR)) {
            return CONDITION_INFERIOR;
        } else if (fieldName.contains(CONDITION_DIFFERENT)) {
            return CONDITION_DIFFERENT;
        } else if (fieldName.contains(CONDITION_LIKE)) {
            return LIKE;
        }
        return CONDITION_EGAL;
    }

    default Boolean isBracketOpen(String fieldName) {
        return fieldName.contains(CONDITION_BRACKET_OPEN) && !fieldName.contains(CONDITION_DOUBLE_BRACKET_OPEN);
    }

    default Boolean isDoubleBracketOpen(String fieldName) {
        return fieldName.contains(CONDITION_DOUBLE_BRACKET_OPEN);
    }

    default Boolean isBracketClose(String fieldName) {
        return fieldName.contains(CONDITION_BRACKET_CLOSE) && !fieldName.contains(CONDITION_DOUBLE_BRACKET_CLOSE);
    }

    default Boolean isDoubleBracketClose(String fieldName) {
        return fieldName.contains(CONDITION_DOUBLE_BRACKET_CLOSE);
    }

    default Boolean isLike(String fieldName) {
        return fieldName.contains(CONDITION_LIKE);
    }

    default Boolean isOr(String fieldName) {
        return fieldName.contains(CONDITATION_OR);
    }
}