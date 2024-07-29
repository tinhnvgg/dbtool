package io.github.vatisteve.metadata.clickhouse;

import io.github.vatisteve.metadata.core.ColumnMetadata;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author tinhnv
 * @since Apr 01, 2024
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ClickHouseColumnMetadata extends ColumnMetadata {

    private int orderByIndex;

}
