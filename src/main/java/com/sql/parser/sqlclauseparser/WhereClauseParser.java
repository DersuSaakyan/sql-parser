package com.sql.parser.sqlclauseparser;

import com.sql.parser.QueryResponse;
import com.sql.parser.sqlclause.WhereClause;

import java.util.List;

public class WhereClauseParser implements SQLClauseParserChain {

    private SQLClauseParserChain chain;

    @Override
    public void setNextParser(SQLClauseParserChain sqlClauseParserChain) {
        this.chain = sqlClauseParserChain;
    }

    @Override
    public void parse(int clauseStartIndex, List<String> queryWords, QueryResponse responseQuery) {
        if (this.shouldSkipCurrentChain(queryWords, clauseStartIndex, ClauseType.WHERE)) {
            chain.parse(clauseStartIndex, queryWords, responseQuery);
            return;
        }

        final int end = this.calculateClauseEndIndex(clauseStartIndex, queryWords, ClauseType.WHERE);

        // Create a single string containing the entire WHERE clause
        final String whereClauseString = String.join(" ", queryWords.subList(clauseStartIndex, end));

        // Remove the "WHERE" keyword from the clause string and trim
        final String whereClause = whereClauseString.replaceFirst("(?i)WHERE", "").trim();

        responseQuery.setWhere(new WhereClause(whereClause));

        chain.parse(end, queryWords, responseQuery);
    }
}
