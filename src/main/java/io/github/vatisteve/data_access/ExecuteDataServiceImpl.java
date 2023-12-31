package io.github.vatisteve.data_access;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ExecuteDataServiceImpl extends SqlQueryServiceCommon implements ExecuteDataService {

    private final Connection connection;

    private List<RowStruct> columns = new ArrayList<>();
    private long totalRowCount = -1;

    private ResultSet createAndExecuteSimpleStatement(String sqlQuery) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sqlQuery);
    }

    @Override
    public List<RowStruct> getColumns(String tableName) throws SQLException {
        List<RowStruct> result = new ArrayList<>();
        String query = describeColumnsNameQuery(schemaName(), tableName, ignoredColumns());
        ResultSet resultSet = createAndExecuteSimpleStatement(query);
        int columnIndex = 0; // start with index 0
        while (resultSet.next()) {
            String columnName = resultSet.getString(columnDescriptions[1]);
            result.add(RowStruct.builder().index(columnIndex++).value(columnName).build());
        }
        this.columns = result;
        return result;
    }

    private String[] ignoredColumns() {
        // TODO: ignored column list
        return new String[0];
    }

    /**
     * Define the database name for each Ds table type
     */
    private String schemaName() throws SQLException {
        // TODO: with specific schema
        return connection.getSchema();
    }

    @Override
    public List<List<RowStruct>> getValues(String tableName, int page, int size) throws SQLException {
        // these column indexes will not be changed after transforming by each stream functions
        String[] columnsNameAsArray = getColumns(tableName).stream().map(RowStruct::getValue).toArray(String[]::new);
        String fetchDataQuery = buildFetchDataQuery(tableName, columnsNameAsArray, page, (long) page * size);
        return doFetchData(fetchDataQuery, columnsNameAsArray);
    }

    private List<List<RowStruct>> doFetchData(String fetchDataQuery, String[] columnsNameAsArray) throws SQLException {
        ResultSet resultSet = createAndExecuteSimpleStatement(fetchDataQuery);
        List<List<RowStruct>> result = new ArrayList<>();
        while (resultSet.next()) {
            List<RowStruct> rowData = new ArrayList<>();
            int i = 0; // start with index 0 for each row, this value is the same as column index
            for (String columnName : columnsNameAsArray) {
                RowStruct cellData = RowStruct.builder()
                    .index(i++)
                    .value(asString(resultSet.getObject(columnName)))
                    .build();
                rowData.add(cellData);
            }
            result.add(rowData);
        }
        return result;
    }

    @Override
    public List<Map<Integer, String>> getValuesAsMap(String tableName, int page, int size) throws SQLException {
        // column indexes must not be changed
        String[] columnsNameAsArray = getColumns(tableName).stream().map(RowStruct::getValue).toArray(String[]::new);
        String fetchDataQuery = buildFetchDataQuery(tableName, columnsNameAsArray, page, (long) page *size);
        return doFetchDataAsMap(fetchDataQuery, columnsNameAsArray);
    }

    private List<Map<Integer, String>> doFetchDataAsMap(String fetchDataQuery, String[] columnsNameAsArray) throws SQLException {
        ResultSet resultSet = createAndExecuteSimpleStatement(fetchDataQuery);
        List<Map<Integer, String>> result = new ArrayList<>();
        while (resultSet.next()) {
            Map<Integer, String> rowData = new LinkedHashMap<>();
            int i = 0; // start with index 0 for each row, the same as column index value
            for (String columnName : columnsNameAsArray) {
                rowData.put(i++, asString(resultSet.getObject(columnName)));
            }
            result.add(rowData);
        }
        return result;
    }

    @Override
    public long totalRowCount(String tableName) throws SQLException {
        ResultSet resultSet = createAndExecuteSimpleStatement(buildCountQuery(tableName));
        if (resultSet.next()) return resultSet.getLong(1);
        return 0;
    }

    @Override
    public DataStructDto fetchData(String tableName, int page, int size) throws SQLException {
        List<RowStruct> tableColumns = getColumns(tableName);
        // these column indexes will not be changed after transforming by each stream functions
        String[] columnsNameAsArray = tableColumns.stream().map(RowStruct::getValue).toArray(String[]::new);
        String fetchDataQuery = buildFetchDataQuery(tableName, columnsNameAsArray, size, (long) page * size);
        List<Map<Integer, String>> tableData = doFetchDataAsMap(fetchDataQuery, columnsNameAsArray);
        return DataStructDto.builder()
            .columns(tableColumns)
            .values(tableData)
            .pagination(new Pagination(totalRowCount(tableName), page, size))
            .build();
    }

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) connection.close();
    }
}
