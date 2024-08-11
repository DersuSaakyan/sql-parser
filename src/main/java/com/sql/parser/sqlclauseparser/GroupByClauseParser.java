package com.sql.parser.sqlclauseparser;

import com.sql.parser.QueryResponse;
import com.sql.parser.sqlclause.GroupByClause;

import java.util.List;

public class GroupByClauseParser implements SQLClauseParserChain {

    private SQLClauseParserChain chain;

    @Override
    public void setNextParser(SQLClauseParserChain sqlClauseParserChain) {
        this.chain = sqlClauseParserChain;
    }

    @Override
    public void parse(int clauseStartIndex, List<String> queryWords, QueryResponse responseQuery) {
        if (this.shouldSkipCurrentChain(queryWords, clauseStartIndex, ClauseType.GROUP_BY)) {
            chain.parse(clauseStartIndex, queryWords, responseQuery);
            return;
        }

        final int end = this.calculateClauseEndIndex(clauseStartIndex, queryWords, ClauseType.GROUP_BY);

        // Create a single string containing the entire GROUP_BY clause
        final String groupByClauseString = String.join(" ", queryWords.subList(clauseStartIndex, end));

        // Remove the "GROUP_BY" keyword from the clause string and trim
        final String groupByClause = groupByClauseString.replaceFirst("(?i)GROUP_BY", "").trim();

        responseQuery.setGroupBy(new GroupByClause(groupByClause));

        chain.parse(end, queryWords, responseQuery);
    }
}
