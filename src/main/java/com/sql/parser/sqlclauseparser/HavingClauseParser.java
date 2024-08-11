package com.sql.parser.sqlclauseparser;

import com.sql.parser.QueryResponse;
import com.sql.parser.sqlclause.HavingClause;

import java.util.List;

public class HavingClauseParser implements SQLClauseParserChain {

    private SQLClauseParserChain chain;

    @Override
    public void setNextParser(SQLClauseParserChain sqlClauseParserChain) {
        this.chain = sqlClauseParserChain;
    }

    @Override
    public void parse(int clauseStartIndex, List<String> queryWords, QueryResponse responseQuery) {
        if (this.shouldSkipCurrentChain(queryWords, clauseStartIndex, ClauseType.HAVING)) {
            chain.parse(clauseStartIndex, queryWords, responseQuery);
            return;
        }

        final int end = this.calculateClauseEndIndex(clauseStartIndex, queryWords, ClauseType.HAVING);

        // Create a single string containing the entire HAVING clause
        final String havingClauseString = String.join(" ", queryWords.subList(clauseStartIndex, end));

        // Remove the "HAVING" keyword from the clause string and trim
        final String havingClause = havingClauseString.replaceFirst("(?i)HAVING", "").trim();

        responseQuery.setHaving(new HavingClause(havingClause));

        chain.parse(end, queryWords, responseQuery);
    }
}
