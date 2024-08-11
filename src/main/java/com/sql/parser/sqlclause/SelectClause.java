package com.sql.parser.sqlclause;

import java.util.List;

public record SelectClause(List<Select> columns) {

    public record Select(String name, String alias) {
    }
}
