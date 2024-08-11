package com.sql.parser.sqlclauseparser;

import com.sql.parser.QueryResponse;
import com.sql.parser.exception.RequiredClauseMissingException;
import com.sql.parser.sqlclause.SelectClause;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class SelectClauseParser implements SQLClauseParserChain {

    private SQLClauseParserChain chain;

    @Override
    public void setNextParser(SQLClauseParserChain sqlClauseParserChain) {
        this.chain = sqlClauseParserChain;
    }

    @Override
    public void parse(int clauseStartIndex, List<String> queryWords, QueryResponse responseQuery) {

        // Validate the query and check if it starts with "SELECT"
        if (!isValidSelectClause(queryWords)) {
            throw new RequiredClauseMissingException("Invalid SQL Query: SELECT clause is missing");
        }

        // Extract the SELECT clause end index
        final int endIndex = this.calculateEndIndexOfSelectClause(queryWords);
        if (endIndex == -1) {
            throw new RequiredClauseMissingException("Invalid SQL Query: From is missing");
        }

        // Join the SELECT clause elements into a string, excluding the "DISTINCT" keyword
        final String columns = queryWords.subList(1, endIndex).stream()
                .filter(item -> !item.equalsIgnoreCase("DISTINCT"))
                .collect(Collectors.joining(" "));

        // Parse the columns into a list of SelectClause.Select objects
        final List<SelectClause.Select> result = this.parseSelectColumns(columns);
        responseQuery.setSelect(new SelectClause(result));

        this.chain.parse(endIndex, queryWords, responseQuery);
    }

    private boolean isValidSelectClause(List<String> queryWords) {
        return !queryWords.isEmpty() && queryWords.get(0).equalsIgnoreCase("SELECT");
    }

    private int calculateEndIndexOfSelectClause(List<String> queryWords) {
        final Stack<String> stack = new Stack<>();
        int end = -1;

        // Iterate through the query words to find the end of the SELECT clause
        for (int i = 0; i < queryWords.size(); i++) {
            final String word = queryWords.get(i);

            // Push "SELECT" (or "(SELECT") onto the stack when encountered
            if (word.equalsIgnoreCase("SELECT") || word.equalsIgnoreCase("(SELECT")) {
                stack.push(word);
            }

            // Pop from the stack when encountering "FROM" to determine the end of the SELECT clause
            else if (word.equalsIgnoreCase("FROM")) {
                String pop = stack.pop();
                if (pop.equalsIgnoreCase("SELECT")) {
                    end = i;
                    break;
                }
            }
        }

        return end;
    }

    private List<SelectClause.Select> parseSelectColumns(String elements) {
        final String[] parts = elements.split(",");
        final List<SelectClause.Select> result = new ArrayList<>();

        // Process each part to identify the column name and alias (if any)
        for (String part : parts) {
            String[] split = part.trim().split("(?i)\\s+as\\s+");
            String column = split[0].trim();
            String alias = (split.length > 1) ? split[1].trim() : null;
            result.add(new SelectClause.Select(column, alias));
        }

        return result;
    }
}
