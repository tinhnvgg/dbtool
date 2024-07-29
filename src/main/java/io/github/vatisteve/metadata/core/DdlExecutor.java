package io.github.vatisteve.metadata.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;

/**
 * @author tinhnv
 * @since Dec 20, 2023
 */
public interface DdlExecutor extends AutoCloseable {

    void createTable() throws SQLException;

    void dropTable() throws SQLException;

    void renameTable(String newName) throws SQLException;

    void addColumn(String columnName) throws SQLException;

    void dropColumn(String columnName) throws SQLException;

    void renameColumn(String oldName, String newName) throws SQLException;

    void updateColumnDefinition(String columnName) throws SQLException;

    void addColumnConstraint(ConstraintType constraintType, String columnName) throws SQLException;

    void dropColumnConstraint(ConstraintType constraintType, String constraintName) throws SQLException;

    Connection getConnection();
    TableMetadata getTableMetadata();
    void logSqlQuery(String sql);

    default void executeSql(String sql) throws SQLException {
        logSqlQuery(sql);
        try (Statement statement = getConnection().createStatement()) {
            statement.execute(sql);
        }
    }

    default <T> Collection<T> collectionNullSafe(Collection<T> collection) {
        if (collection == null) return Collections.emptyList();
        return collection;
    }

}
