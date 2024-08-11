package com.sql.parser.sqlclauseparser;

import java.util.Arrays;
import java.util.List;

public enum ClauseType {
    SELECT(1),
    FROM(2),
    INNER_JOIN(3),
    RIGHT_JOIN(3),
    LEFT_JOIN(3),
    FULL_JOIN(3),
    CROSS_JOIN(3),
    WHERE(4),
    GROUP_BY(5),
    HAVING(6),
    ORDER_BY(7),
    LIMIT(8),
    OFFSET(9);

    ClauseType(int order) {
        this.order = order;
    }

    private final int order;

    public int getOrder() {
        return order;
    }

    public static List<ClauseType> getNextClauses(ClauseType clauseType) {
        return Arrays.stream(values()).filter(item -> item.getOrder() > clauseType.getOrder()).toList();
    }
}
