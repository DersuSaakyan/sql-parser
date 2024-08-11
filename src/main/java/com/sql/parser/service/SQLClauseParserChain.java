package com.sql.parser.service;

import com.sql.parser.sqlclauseparser.*;

public class SQLClauseParserChain {

    private final SelectClauseParser selectClauseParser;

    public SQLClauseParserChain() {
        this.selectClauseParser = new SelectClauseParser();
        final FromClauseParser fromClauseParser = new FromClauseParser();
        final JoinClauseParser joinClauseParser = new JoinClauseParser();
        final WhereClauseParser whereClauseParser = new WhereClauseParser();
        final GroupByClauseParser groupByClauseParser = new GroupByClauseParser();
        final HavingClauseParser havingClauseParser = new HavingClauseParser();
        final OrderByClauseParser orderByClauseParser = new OrderByClauseParser();
        final LimitClauseParser limitClauseParser = new LimitClauseParser();
        final OffsetClauseParser offsetClauseParser = new OffsetClauseParser();

        selectClauseParser.setNextParser(fromClauseParser);
        fromClauseParser.setNextParser(joinClauseParser);
        joinClauseParser.setNextParser(whereClauseParser);
        whereClauseParser.setNextParser(groupByClauseParser);
        groupByClauseParser.setNextParser(havingClauseParser);
        havingClauseParser.setNextParser(orderByClauseParser);
        orderByClauseParser.setNextParser(limitClauseParser);
        limitClauseParser.setNextParser(offsetClauseParser);

        offsetClauseParser.setNextParser(new FinishChainClauseParser());
    }

    public SelectClauseParser getFistChainElement() {
        return selectClauseParser;
    }
}
