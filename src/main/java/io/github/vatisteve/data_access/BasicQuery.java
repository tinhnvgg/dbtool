package io.github.vatisteve.data_access;

/**
 * @author tinhnv
 * @since Oct 31, 2023
 *
 * @apiNote used for mariadb, mysql query format. Create interfaces for more implementation
 */
// TODO more flexible, separate the select, where and other clauses before building sql query
public class BasicQuery extends SqlQueryConstants {

    private final StringBuilder sqlQuery;

    private boolean selectClauseIsUsed = false;

    public BasicQuery() {
        this.sqlQuery = new StringBuilder();
    }

    private void throwQueryBuilderException() {
        throw new QueryBuilderException(String.format("There are conflicts when building SQL query: %s", sqlQuery));
    }

    public BasicQuery select(String... elements) {
        sqlQuery.append(SELECT_KEYWORD).append(String.join(COMMA + SPACE, elements)).append(SPACE);
        if (selectClauseIsUsed) throwQueryBuilderException();
        selectClauseIsUsed = true;
        return this;
    }

    public BasicQuery selectMax(String element) {
        sqlQuery.append(SELECT_KEYWORD).append(String.format(MAX_FORMAT, element)).append(SPACE);
        if (selectClauseIsUsed) throwQueryBuilderException();
        selectClauseIsUsed = true;
        return this;
    }

    public BasicQuery countAll() {
        sqlQuery.append(SELECT_KEYWORD).append(COUNT_ALL).append(SPACE);
        if (selectClauseIsUsed) throwQueryBuilderException();
        selectClauseIsUsed = true;
        return this;
    }

    // TODO update this method
    public BasicQuery from(String table) {
        sqlQuery.append(FROM_KEYWORD).append(table).append(SPACE);
        return this;
    }

    public BasicQuery where(BasicCriteria criteria) {
        sqlQuery.append(WHERE_KEYWORD).append(criteria.toCriteriaString()).append(SPACE);
        return this;
    }

    public BasicQuery limit(int limit) {
        sqlQuery.append(LIMIT_KEYWORD).append(limit).append(SPACE);
        return this;
    }

    public BasicQuery offset(long offset) {
        sqlQuery.append(OFFSET_KEYWORD).append(offset).append(SPACE);
        return this;
    }

    public String toQueryString() {
        return sqlQuery.toString();
    }

}
