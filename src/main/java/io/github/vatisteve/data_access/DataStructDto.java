package io.github.vatisteve.data_access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Sample data struct with header list
 *
 * @author tinhnv
 * @since Oct 31, 2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataStructDto {

    private List<RowStruct> columns;
    private List<Map<Integer, String>> values;
    private Pagination pagination;

}
