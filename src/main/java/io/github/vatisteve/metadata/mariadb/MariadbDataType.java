package io.github.vatisteve.metadata.mariadb;

import io.github.vatisteve.metadata.core.DataType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static io.github.vatisteve.metadata.core.DataType.BasicDataType.*;

/**
 * @author tinhnv
 * @since Dec 19, 2023
 */
@Getter
@RequiredArgsConstructor
public enum MariadbDataType implements DataType {

    TINYINT(NUMERIC, "Very small integer", true),
    MEDIUMINT(NUMERIC, "Medium-sized integer", true),
    INT(NUMERIC, "Standard integer", true),
    BIGINT(NUMERIC, "Large integer", true),
    DECIMAL(NUMERIC, "Fixed-point number", true),
    FLOAT(NUMERIC, "Single-precision floating-point number", true),
    DOUBLE(NUMERIC, "Double-precision floating-point number", true),
    BIT(NUMERIC, "A bit", true),

    CHAR(STRING, "Fixed-length non-binary (character) string", true),
    VARCHAR(STRING, "Variable-length non-binary string", true),
    BINARY(STRING, "Fixed-length binary string", true),
    VARBINARY(STRING, "Variable-length binary string", true),
    TINYBLOB(STRING, "Very small BLOB (binary large object)", true),
    BLOB(STRING, "Small BLOB", true),
    MEDIUMBLOB(STRING, "Medium-sized BLOB", true),
    LONGBLOB(STRING, "Large BLOB", true),
    TINYTEXT(STRING, "Very small non-binary string", true),
    TEXT(STRING, "Small non-binary string", true),
    MEDIUMTEXT(STRING, "Medium-sized non-binary string", true),
    LONGTEXT(STRING, "Large non-binary string", true),
    ENUM(STRING, "Enumeration", true),
    SET(STRING, "A set", true),

    DATE(TEMPORAL, "Date value in CCYY-MM-DD format", true),
    TIME(TEMPORAL, "Time value in hh:mm:ss format", true),
    DATETIME(TEMPORAL, "Date and time value in CCYY-MM-DD hh:mm:ss format", true),
    TIMESTAMP(TEMPORAL, "Timestamp value in CCYY-MM-DD hh:mm:ss format", true),
    YEAR(TEMPORAL, "Year value in CCYY or YY format", true),

    GEOMETRY(SPATIAL, "Spatial value of any type", true),
    POINT(SPATIAL, "Point (a pair of X-Y coordinates)", true),
    LINESTRING(SPATIAL, "Curve (one or more POINT values)", true),
    POLYGON(SPATIAL, "Polygon", true),
    GEOMETRYCOLLECTION(SPATIAL, "Collection of GEOMETRY values", true),
    MULTILINESTRING(SPATIAL, "Collection of LINESTRING values", true),
    MULTIPOINT(SPATIAL, "Collection of POINT values", true),
    MULTIPOLYGON(SPATIAL, "Collection of POLYGON values", true),

    ;

    private final DataType parent;
    private final String description;
    private final boolean enable;

}
