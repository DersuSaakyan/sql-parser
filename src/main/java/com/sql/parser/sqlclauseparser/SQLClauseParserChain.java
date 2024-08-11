package com.sql.parser.sqlclauseparser;

import com.sql.parser.QueryResponse;

import java.util.List;

public interface SQLClauseParserChain {

    void setNextParser(SQLClauseParserChain sqlClauseParserChain);

    void parse(int clauseStartIndex, List<String> queryWords, QueryResponse responseQuery);

    default boolean shouldSkipCurrentChain(List<String> queryWords, int clauseStartIndex, ClauseType clauseType) {
        if (clauseStartIndex > queryWords.size() - 1) {
            return true;
        }

        return !queryWords.get(clauseStartIndex).equalsIgnoreCase(clauseType.name());
    }

    default int calculateClauseEndIndex(int clauseStartIndex, List<String> queryWords, ClauseType clauseType) {
        final List<String> nextClauses = ClauseType.getNextClauses(clauseType)
                .stream()
                .map(ClauseType::name)
                .toList();

        for (int i = clauseStartIndex; i < queryWords.size(); i++) {
            final String word = queryWords.get(i);
            if (nextClauses.stream().anyMatch(item -> item.equalsIgnoreCase(word))) {
                return i;
            }
        }

        return queryWords.size();
    }
}
