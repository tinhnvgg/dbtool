package io.github.vatisteve.metadata.core;

import io.github.vatisteve.dataaccess.SqlQueryConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author tinhnv
 * @since Dec 20, 2023
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DdlQueryConstants extends SqlQueryConstants {

    protected static final String CREATE = "CREATE";
    protected static final String ALTER = "ALTER";
    protected static final String DROP = "DROP";
    protected static final String ADD = "ADD";

    protected static final String ALTER_TABLE = ALTER + " TABLE ";

    protected static final String PRIMARY_KEY = "PRIMARY KEY";
    protected static final String FOREIGN_KEY = "FOREIGN KEY";
    protected static final String REFERENCES = "REFERENCES";

}
