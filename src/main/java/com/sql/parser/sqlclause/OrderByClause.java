package com.sql.parser.sqlclause;

import java.util.List;

public record OrderByClause(List<Order> orders, String direction) {

    public record Order(String column) {
    }
}
