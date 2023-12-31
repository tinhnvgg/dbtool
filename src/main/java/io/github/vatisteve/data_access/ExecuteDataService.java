package io.github.vatisteve.data_access;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author tinhnv
 * @since Oct 31, 2023
 */
public interface ExecuteDataService extends AutoCloseable {

    List<RowStruct> getColumns(String tableName) throws SQLException;

    List<List<RowStruct>> getValues(String tableName, int page, int size) throws SQLException;

    List<Map<Integer, String>> getValuesAsMap(String tableName, int page, int size) throws SQLException;

    long totalRowCount(String tableName) throws SQLException;

    DataStructDto fetchData(String tableName, int page, int size) throws SQLException;

}
