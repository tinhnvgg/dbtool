package io.github.vatisteve.data_access;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author tinhnv
 * @since Oct 31, 2023
 *
 * @apiNote used for mariadb, mysql query format
 */
public class BasicCriteria extends SqlQueryConstants {

    private final StringBuilder criteria;

    public BasicCriteria(String criteria) {
        this.criteria = new StringBuilder(criteria);
    }

    public BasicCriteria equalWithSingleQuote(String query) {
        criteria.append(SPACE).append(EQUAL).append(SPACE).append(SINGLE_QUOTE).append(query).append(SINGLE_QUOTE).append(SPACE);
        return this;
    }

    public BasicCriteria equalWithNumber(Number number) {
        criteria.append(SPACE).append(EQUAL).append(SPACE).append(number).append(SPACE);
        return this;
    }

    public BasicCriteria and(String query) {
        criteria.append(SPACE).append(AND_KEYWORD).append(SPACE).append(query).append(SPACE);
        return this;
    }

    public BasicCriteria andFormat(String query) {
        criteria.append(SPACE).append(String.format(AND_FORMAT, query)).append(SPACE);
        return this;
    }

    public BasicCriteria inFormat(String... elements) {
        String wrappedElement = Arrays.stream(elements).map(SqlQueryConstants::wrapWithSingleQuote).collect(Collectors.joining(COMMA + SPACE));
        criteria.append(SPACE).append(String.format(IN_FORMAT, wrappedElement)).append(SPACE);
        return this;
    }

    public BasicCriteria notInFormat(String... elements) {
        String wrappedElement = Arrays.stream(elements).map(SqlQueryConstants::wrapWithSingleQuote).collect(Collectors.joining(COMMA + SPACE));
        criteria.append(SPACE).append(String.format(NOT_IN_FORMAT, wrappedElement)).append(SPACE);
        return this;
    }

    public BasicCriteria append(String s) {
        criteria.append(SPACE).append(s).append(SPACE);
        return this;
    }

    public String toCriteriaString() {
        return criteria.toString();
    }
}
