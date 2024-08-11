package com.sql.parser.sqlclauseparser;

import com.sql.parser.QueryResponse;

import java.util.List;

// Null object
public class FinishChainClauseParser implements SQLClauseParserChain {

    @Override
    public void setNextParser(SQLClauseParserChain sqlClauseParserChain) {

    }

    @Override
    public void parse(int clauseStartIndex, List<String> queryWords, QueryResponse responseQuery) {

    }
}
