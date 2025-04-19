package org.vagabond.engine.crud.utils.query;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class IQueryUtilsTest {

    private IQueryUtils tested = new IQueryUtils() {
    };

    private String getSqlResult(String fields, Object... values) {
        return getSqlResult(true, fields, values);
    }

    private String getSqlResult(Boolean active, String fields, Object... values) {
        StringBuilder endQuery = new StringBuilder();
        List<QueryUtilsDto> queryDtos = tested.initQueryDto(endQuery, fields, values);
        return tested.getSqlQuery(queryDtos, active, endQuery);
    }

    @Test
    void given_QueryUtils_when_initQueryDtoWithConditionEquals_then_ReturnSqlRequest() {
        String sqlQuery = getSqlResult("username", "test");
        assertEquals(" WHERE UPPER(username) = UPPER(?1) and e.active = true", sqlQuery);
    }

    @Test
    void given_QueryUtils_when_initQueryDtoWithEndQuery_then_ReturnSqlRequest() {
        StringBuilder endQuery = new StringBuilder();
        List<QueryUtilsDto> queryDtos = tested.initQueryDto(endQuery, "username", "test");
        String sqlQuery = tested.getSqlQuery(queryDtos, true, endQuery);
        assertEquals(" WHERE UPPER(username) = UPPER(?1) and e.active = true", sqlQuery);
    }

    @Test
    void given_QueryUtils_when_initQueryDtoWithOrder_then_ReturnSqlRequest() {
        String sqlQuery = getSqlResult("username>>ordre", "plouf");
        assertEquals(" WHERE UPPER(username) = UPPER(?1) and e.active = true order by ordre", sqlQuery);
    }

    @Test
    void given_QueryUtils_when_initQueryDtoWithGroupBy_then_ReturnSqlRequest() {
        String sqlQuery = getSqlResult("username<<ordre", "plouf");
        assertEquals(" WHERE UPPER(username) = UPPER(?1) and e.active = true group by ordre", sqlQuery);
    }

    @Test
    void given_QueryUtils_when_initQueryDtoWithLikeAndOrder_then_ReturnSqlRequest() {
        String sqlQuery = getSqlResult("username%AndActive<<ordre", "plouf", "false");
        assertEquals(" WHERE UPPER(username) like UPPER(?1) AND active = ?2 and e.active = true group by ordre", sqlQuery);
    }

    @Test
    void given_QueryUtils_when_initQueryDtoWithSuperior_then_ReturnSqlRequest() {
        String sqlQuery = getSqlResult("user.idAnd(usernameAnd|Date>)AndProduct.name", "1", "test", "2015", "plouf");
        assertEquals(
                " WHERE user.id = ?1 AND ( UPPER(username) = UPPER(?2) OR date > ?3 ) AND UPPER(product.name) = UPPER(?4) and e.active = true",
                sqlQuery);
    }

    @Test
    void given_QueryUtils_when_initQueryDtoWithSuperiorDate_then_ReturnSqlRequest() {
        String sqlQuery = getSqlResult(false, "user.idAnd(usernameAnd|Date>=)AndProduct.name", "1", "test", "2015", "plouf");
        assertEquals(" WHERE user.id = ?1 AND ( UPPER(username) = UPPER(?2) OR date >= ?3 ) AND UPPER(product.name) = UPPER(?4)", sqlQuery);
    }

    @Test
    void given_QueryUtils_when_initQueryDtoWitAllDate_then_ReturnSqlRequest() {
        String sqlQuery = getSqlResult(false, "user.idAnd((usernameAnd|Date>=)AndProduct.name)AndDate<AndDate<=AndDate>AndDate#", "1",
                "test", "2015", "plouf", "plouf", "plouf", "plouf", "plouf", "plouf");
        assertEquals(
                " WHERE user.id = ?1 AND (( UPPER(username) = UPPER(?2) OR date >= ?3 ) AND UPPER(product.name) = UPPER(?4) ) AND UPPER(date) < UPPER(?5) AND UPPER(date) <= UPPER(?6) AND UPPER(date) > UPPER(?7) AND UPPER(date) = UPPER(?8)",
                sqlQuery);
    }

    @Test
    void given_QueryUtils_when_initQueryDtoWitAllDate_then_ReturnSqlRequest2() {
        String sqlQuery = getSqlResult(false, "(name%And|Function%And|User.username%)AndActiveAnd(IsPrivate~And|IsPrivate!~)", "1", "test",
                "2015", "plouf", "null", "true", "null", "true");
        assertEquals(
                " WHERE ( UPPER(name) like UPPER(?1) OR UPPER(function) like UPPER(?2) OR UPPER(user.username) like UPPER(?3) ) AND UPPER(active) = UPPER(?4) AND ( isPrivate is null OR isPrivate is not true )",
                sqlQuery);
    }
}