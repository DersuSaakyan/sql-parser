package com.sql.parser.sqlclauseparser;

import com.sql.parser.QueryResponse;
import com.sql.parser.sqlclause.FromClause;

import java.util.ArrayList;
import java.util.List;

public class FromClauseParser implements SQLClauseParserChain {

    private SQLClauseParserChain chain;

    @Override
    public void setNextParser(SQLClauseParserChain sqlClauseParserChain) {
        this.chain = sqlClauseParserChain;
    }

    @Override
    public void parse(int clauseStartIndex, List<String> queryWords, QueryResponse responseQuery) {
        final int end = this.calculateClauseEndIndex(clauseStartIndex, queryWords, ClauseType.FROM);

        // Create a single string containing the entire FROM clause
        final String fromClauseString = String.join(" ", queryWords.subList(clauseStartIndex, end));

        // Remove the "FROM" keyword from the clause string and trim
        final String fromClause = fromClauseString.replaceFirst("(?i)FROM", "").trim();

        // Split the "FROM" clause string into individual table entries by commas
        final String[] fromClauseArr = fromClause.split(",");

        // Loop through each table entry, split into table name and alias, and add to the list
        final List<FromClause.From> froms = new ArrayList<>();
        for (String s : fromClauseArr) {
            final String[] tableAliasArr = s.trim().split("\\s+");
            froms.add(new FromClause.From(tableAliasArr[0].trim(), tableAliasArr.length > 1 ? tableAliasArr[1].trim() : null));
        }

        responseQuery.setFrom(new FromClause(froms));

        this.chain.parse(end, queryWords, responseQuery);
    }
}
