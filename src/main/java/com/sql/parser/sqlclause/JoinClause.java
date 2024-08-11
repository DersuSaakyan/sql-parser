package com.sql.parser.sqlclause;

import java.util.List;

public record JoinClause(List<Join> joins) {

    public record Join(String type, String tableWithCondition) {
    }
}