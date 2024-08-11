package com.sql.parser.sqlclauseparser;

import com.sql.parser.QueryResponse;
import com.sql.parser.sqlclause.JoinClause;

import java.util.ArrayList;
import java.util.List;

public class JoinClauseParser implements SQLClauseParserChain {

    private final List<String> SUPPORTED_JOINS = List.of(ClauseType.INNER_JOIN.name(),
            ClauseType.RIGHT_JOIN.name(),
            ClauseType.LEFT_JOIN.name(),
            ClauseType.FULL_JOIN.name(),
            ClauseType.CROSS_JOIN.name());

    private SQLClauseParserChain chain;

    @Override
    public void setNextParser(SQLClauseParserChain sqlClauseParserChain) {
        this.chain = sqlClauseParserChain;
    }

    @Override
    public void parse(int clauseStartIndex, List<String> queryWords, QueryResponse responseQuery) {
        final String joinType = queryWords.get(clauseStartIndex);

        // Check if the join is existing, if not, pass control to the next parser in the chain
        if (SUPPORTED_JOINS.stream().noneMatch(item -> item.equalsIgnoreCase(joinType.toUpperCase()))) {
            chain.parse(clauseStartIndex, queryWords, responseQuery);
            return;
        }

        final int end = this.calculateClauseEndIndex(clauseStartIndex, queryWords, ClauseType.valueOf(joinType.toUpperCase()));

        // Create a single string containing the entire JOIN clause
        final String joinClauseString = String.join(" ", queryWords.subList(clauseStartIndex, end));

        // Split the JOIN clause string into individual JOIN statements
        final String[] joinClauseArr = joinClauseString.split("(?i)\\s+(?=INNER_JOIN|RIGHT_JOIN|LEFT_JOIN|FULL_JOIN|CROSS_JOIN)");

        final List<JoinClause.Join> joins = new ArrayList<>();

        // Iterate over the split JOIN statements
        for (String s : joinClauseArr) {
            final String[] joinWithCondition = s.split("(?i)JOIN");

            final String clearJoinName = joinWithCondition[0].replace("_", "").trim();
            final String condition = joinWithCondition[1].trim();

            joins.add(new JoinClause.Join(clearJoinName, condition));
        }

        responseQuery.setJoin(new JoinClause(joins));
        this.chain.parse(end, queryWords, responseQuery);
    }
}
