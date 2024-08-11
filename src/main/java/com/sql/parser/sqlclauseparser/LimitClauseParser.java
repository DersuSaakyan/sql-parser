package com.sql.parser.sqlclauseparser;

import com.sql.parser.QueryResponse;
import com.sql.parser.sqlclause.LimitClause;

import java.util.List;

public class LimitClauseParser implements SQLClauseParserChain {

    private SQLClauseParserChain chain;

    @Override
    public void setNextParser(SQLClauseParserChain sqlClauseParserChain) {
        this.chain = sqlClauseParserChain;
    }

    @Override
    public void parse(int clauseStartIndex, List<String> queryWords, QueryResponse responseQuery) {
        if (this.shouldSkipCurrentChain(queryWords, clauseStartIndex, ClauseType.LIMIT)) {
            chain.parse(clauseStartIndex, queryWords, responseQuery);
            return;
        }

        final int end = this.calculateClauseEndIndex(clauseStartIndex, queryWords, ClauseType.LIMIT);

        // Create a single string containing the entire LIMIT clause
        final String limitClauseString = String.join(" ", queryWords.subList(clauseStartIndex, end));

        // Remove the "LIMIT" keyword from the clause string and trim
        final String limitClause = limitClauseString.replaceFirst("(?i)LIMIT", "").trim();

        responseQuery.setLimit(new LimitClause(limitClause));

        chain.parse(end, queryWords, responseQuery);
    }
}
