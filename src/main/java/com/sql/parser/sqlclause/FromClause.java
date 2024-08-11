package com.sql.parser.sqlclause;

import java.util.List;

public record FromClause(List<From> froms) {

    public record From(String tableName, String alias) {
    }
}
