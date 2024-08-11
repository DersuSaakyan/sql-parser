package com.sql.parser.sqlclauseparser;

import com.sql.parser.QueryResponse;
import com.sql.parser.sqlclause.OffsetClause;

import java.util.List;

public class OffsetClauseParser implements SQLClauseParserChain {

    private SQLClauseParserChain chain;

    @Override
    public void setNextParser(SQLClauseParserChain sqlClauseParserChain) {
        this.chain = sqlClauseParserChain;
    }

    @Override
    public void parse(int clauseStartIndex, List<String> queryWords, QueryResponse responseQuery) {
        if (this.shouldSkipCurrentChain(queryWords, clauseStartIndex, ClauseType.OFFSET)) {
            chain.parse(clauseStartIndex, queryWords, responseQuery);
            return;
        }

        int end = this.calculateClauseEndIndex(clauseStartIndex, queryWords, ClauseType.OFFSET);

        // Create a single string containing the entire OFFSET clause
        final String offsetClauseString = String.join(" ", queryWords.subList(clauseStartIndex, end));

        // Remove the "OFFSET" keyword from the clause string and trim
        final String offsetClause = offsetClauseString.replaceFirst("(?i)OFFSET", "").trim();

        responseQuery.setOffset(new OffsetClause(offsetClause));

        chain.parse(end, queryWords, responseQuery);
    }
}
