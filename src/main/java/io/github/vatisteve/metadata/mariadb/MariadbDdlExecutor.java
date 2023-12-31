package io.github.vatisteve.metadata.mariadb;

import io.github.vatisteve.metadata.core.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tinhnv
 * @since Dec 20, 2023
 */
@RequiredArgsConstructor
@Getter
public class MariadbDdlExecutor extends DdlQueryConstants implements DdlExecutor {

    private final TableMetadata tableMetadata;
    private final Connection connection;

    // should move to utils class
    public static <T> Collection<T> collectionNullSafe(Collection<T> collection) {
        if (collection == null) return Collections.emptyList();
        return collection;
    }

    private static void removeTheLastComma(StringBuilder sql) {
        // sql contain 'append(COMMA).append(SPACE)'
        sql.replace(sql.length() - 2, sql.length(), SPACE);
    }

    @Override
    public void createTable() throws SQLException {
        StringBuilder sql = new StringBuilder("CREATE TABLE ").append(backtickWrap(tableMetadata.getName()))
            .append(OPEN_BRACKET).append(SPACE);
        List<String> prs = new ArrayList<>();
        Map<String, ReferenceMetadata> ref = new HashMap<>();
        collectionNullSafe(tableMetadata.getColumnsMetadata()).forEach(c -> {
            appendColumnSql(sql, c);
            sql.append(COMMA).append(SPACE);
            if (c.isPrimaryKey()) prs.add(c.getName());
            if (c.getReference() != null) ref.put(c.getName(), c.getReference());
        });
        if (prs.isEmpty() && ref.isEmpty()) {
            removeTheLastComma(sql);
        } else {
            sql.append(PRIMARY_KEY).append(OPEN_BRACKET)
                .append(prs.stream().map(DdlQueryConstants::backtickWrap).collect(Collectors.joining(COMMA + SPACE)))
                .append(CLOSE_BRACKET);
        }
        if (!ref.isEmpty()) {
            ref.forEach((columnName, refInfo) -> {
                appendForeignKeyConstraint(sql, columnName, refInfo);
                sql.append(COMMA);
            });
            removeTheLastComma(sql);
        }
        sql.append(CLOSE_BRACKET);
        if (tableMetadata.getTablespace() != null) sql.append(" Engine = ").append(tableMetadata.getTablespace());
        //...
        executeSql(sql.toString());
    }

    private void appendColumnSql(StringBuilder sql, ColumnMetadata c) {
        sql.append(backtickWrap(c.getName())).append(SPACE).append(c.getDataType());
        if (!c.isNullable()) sql.append(" NOT NULL ");
        if (c.isIdentity()) sql.append(" AUTO_INCREMENT ");
        if (c.getColumnDefault() != null) appendDefaultColumnValue(sql, c.getColumnDefault());
        if (c.getCheckConstraint() != null) sql.append(" CHECK ")
            .append(OPEN_BRACKET).append(c.getCheckConstraint()).append(CLOSE_BRACKET);
    }

    private void appendDefaultColumnValue(StringBuilder sql, ColumnMetadata.DefaultColumnValue d) {
        sql.append(" DEFAULT ");
        Optional.ofNullable(d.getDataType()).ifPresentOrElse(dt -> {
            if (dt instanceof DataType.BasicDataType) {
                doAppendDefaultColumnValue(sql, d, (DataType.BasicDataType) dt);
            } else if (dt.getParent() instanceof DataType.BasicDataType) {
                doAppendDefaultColumnValue(sql, d, (DataType.BasicDataType) dt.getParent());
            } else {
                sql.append(d.getValue());
            }
        }, () -> sql.append(d.getValue()));
    }

    private static void doAppendDefaultColumnValue(StringBuilder sql, ColumnMetadata.DefaultColumnValue d, DataType.BasicDataType basicDataType) {
        switch (basicDataType) {
            case STRING:
            case SPATIAL:
                sql.append(wrapWithSingleQuote(d.getValue().toString()));
                break;
            case NUMERIC:
            case TEMPORAL:
                sql.append(d.getValue().toString());
                break;
            default: //...
        }
    }

    private void appendForeignKeyConstraint(StringBuilder sql, String cn, ReferenceMetadata ref) {
        sql.append(FOREIGN_KEY).append(SPACE).append(OPEN_BRACKET).append(cn).append(CLOSE_BRACKET)
            .append(REFERENCES).append(SPACE).append(ref.getTableName()).append(OPEN_BRACKET)
            .append(ref.getColumnName()).append(CLOSE_BRACKET);
        if (ref.getOnDelete() != null) sql.append(" ON DELETE ").append(ref.getOnDelete().getLabel().toUpperCase());
        if (ref.getOnUpdate() != null) sql.append(" ON UPDATE ").append(ref.getOnDelete().getLabel().toUpperCase());
    }

    @Override
    public void dropTable() throws SQLException {
        executeSql("DROP TABLE " + tableMetadata.getName());
    }

    @Override
    public void renameTable(String newName) throws SQLException {
        executeSql("RENAME TABLE " + tableMetadata.getName() + " TO " + newName);
    }

    @Override
    public void addColumn(String columnName) throws SQLException {
        ColumnMetadata columnMetadata = getColumnMetadata(columnName);
        StringBuilder sql = new StringBuilder(ALTER_TABLE).append(tableMetadata.getName()).append(" ADD COLUMN ");
        appendColumnSql(sql, columnMetadata);
        executeSql(sql.toString());
    }

    private ColumnMetadata getColumnMetadata(String columnName) {
        return collectionNullSafe(tableMetadata.getColumnsMetadata()).stream()
            .filter(c -> columnName.equals(c.getName()))
            .findFirst().orElseThrow();
    }

    @Override
    public void dropColumn(String columnName) throws SQLException {
        executeSql(ALTER_TABLE + tableMetadata.getName() + " DROP COLUMN " + columnName);
    }

    @Override
    public void renameColumn(String oldName, String newName) throws SQLException {
        executeSql(ALTER_TABLE + tableMetadata.getName() + " RENAME COLUMN " + oldName + " TO " + newName);
    }

    @Override
    public void updateColumnDefinition(String columnName) throws SQLException {
        ColumnMetadata columnMetadata = getColumnMetadata(columnName);
        StringBuilder sql = new StringBuilder(ALTER_TABLE).append(tableMetadata.getName()).append(" MODIFY ");
        appendColumnSql(sql, columnMetadata);
        executeSql(sql.toString());
    }

    @Override
    public void addColumnConstraint(ConstraintType constraintType, String columnName) {
        // TODO add column constraint
        throw new UnsupportedOperationException("This function has not been implemented yet!");
    }

    @Override
    public void logSqlQuery(String sql) {
        // TODO: log
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
