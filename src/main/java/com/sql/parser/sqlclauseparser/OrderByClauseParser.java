package com.sql.parser.sqlclauseparser;

import com.sql.parser.QueryResponse;
import com.sql.parser.sqlclause.OrderByClause;

import java.util.ArrayList;
import java.util.List;

public class OrderByClauseParser implements SQLClauseParserChain {

    private SQLClauseParserChain chain;

    @Override
    public void setNextParser(SQLClauseParserChain sqlClauseParserChain) {
        this.chain = sqlClauseParserChain;
    }

    @Override
    public void parse(int clauseStartIndex, List<String> queryWords, QueryResponse responseQuery) {
        if (this.shouldSkipCurrentChain(queryWords, clauseStartIndex, ClauseType.ORDER_BY)) {
            chain.parse(clauseStartIndex, queryWords, responseQuery);
            return;
        }

        final int end = this.calculateClauseEndIndex(clauseStartIndex, queryWords, ClauseType.ORDER_BY);

        // Create a single string containing the entire ORDER_BY clause
        final String joinedOrderByClause = String.join(" ", queryWords.subList(clauseStartIndex, end));

        // Remove the "ORDER_BY" keyword from the clause string and trim
        final String orderByClauseString = joinedOrderByClause.replaceFirst("(?i)ORDER_BY", "").trim();
        final String[] orderByClauseArr = orderByClauseString.split("\\s+");

        final List<OrderByClause.Order> orders = new ArrayList<>();
        for (int i = 0; i < orderByClauseArr.length - 1; i++) {
            final String column = orderByClauseArr[i].replace(",", "");
            orders.add(new OrderByClause.Order(column));
        }

        final String lastElement = orderByClauseArr[orderByClauseArr.length - 1];
        final OrderByClause orderByClause;

        // Check if the last element is a valid direction (ASC or DESC)
        if (lastElement.equalsIgnoreCase("ASC") || lastElement.equalsIgnoreCase("DESC")) {
            orderByClause = new OrderByClause(orders, lastElement);
        } else {

            // If the last element is not a direction, treat it as another column and add it to the orders list
            orders.add(new OrderByClause.Order(lastElement));
            // Set the default direction as ASC
            orderByClause = new OrderByClause(orders, "ASC");
        }

        responseQuery.setOrderBy(orderByClause);
        chain.parse(end, queryWords, responseQuery);
    }
}
