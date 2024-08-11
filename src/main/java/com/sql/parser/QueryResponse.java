package com.sql.parser;

import com.sql.parser.sqlclause.*;

public class QueryResponse {
    private SelectClause select;
    private FromClause from;
    private JoinClause join;
    private WhereClause where;
    private GroupByClause groupBy;
    private HavingClause having;
    private OrderByClause orderBy;
    private LimitClause limit;
    private OffsetClause offset;

    public SelectClause getSelect() {
        return select;
    }

    public void setSelect(SelectClause select) {
        this.select = select;
    }

    public FromClause getFrom() {
        return from;
    }

    public void setFrom(FromClause from) {
        this.from = from;
    }

    public JoinClause getJoin() {
        return join;
    }

    public void setJoin(JoinClause join) {
        this.join = join;
    }

    public WhereClause getWhere() {
        return where;
    }

    public void setWhere(WhereClause where) {
        this.where = where;
    }

    public GroupByClause getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(GroupByClause groupBy) {
        this.groupBy = groupBy;
    }

    public HavingClause getHaving() {
        return having;
    }

    public void setHaving(HavingClause having) {
        this.having = having;
    }

    public OrderByClause getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderByClause orderBy) {
        this.orderBy = orderBy;
    }

    public LimitClause getLimit() {
        return limit;
    }

    public void setLimit(LimitClause limit) {
        this.limit = limit;
    }

    public OffsetClause getOffset() {
        return offset;
    }

    public void setOffset(OffsetClause offset) {
        this.offset = offset;
    }
}
