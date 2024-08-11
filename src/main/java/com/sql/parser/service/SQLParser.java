package com.sql.parser.service;

import com.sql.parser.QueryResponse;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLParser {

    public static QueryResponse parse(String sql) {
        if (sql == null || sql.isBlank()) {
            return new QueryResponse();
        }

        final StringBuilder stringBuilder = new StringBuilder(sql);

        replaceAllIgnoreCase(stringBuilder, "GROUP\\s+BY", "group_by");
        replaceAllIgnoreCase(stringBuilder, "ORDER\\s+BY", "order_by");

        replaceAllIgnoreCase(stringBuilder, "INNER\\s+JOIN", "inner_join");
        replaceAllIgnoreCase(stringBuilder, "RIGHT\\s+JOIN", "right_join");
        replaceAllIgnoreCase(stringBuilder, "LEFT\\s+JOIN", "left_join");
        replaceAllIgnoreCase(stringBuilder, "FULL\\s+JOIN", "full_join");
        replaceAllIgnoreCase(stringBuilder, "CROSS\\s+JOIN", "cross_join");


        final SQLClauseParserChain chain = new SQLClauseParserChain();
        final List<String> queryWords = Arrays.asList(stringBuilder.toString().split("\\s+"));

        final QueryResponse queryResponse = new QueryResponse();
        chain.getFistChainElement().parse(0, queryWords, queryResponse);

        return queryResponse;
    }

    private static void replaceAllIgnoreCase(StringBuilder builder, String regex, String replacement) {
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(builder);
        int start = 0;
        while (matcher.find(start)) {
            String originString = matcher.group(0);
            builder.replace(matcher.start(), matcher.end(), applyCase(originString, replacement));
            start = matcher.start() + replacement.length();
        }
    }

    // Method to apply the case of the template string to the target string
    public static String applyCase(String template, String target) {
        // Determine the case of the template string
        final boolean isTemplateUpperCase = template.equals(template.toUpperCase());
        final boolean isTemplateLowerCase = template.equals(template.toLowerCase());

        if (isTemplateUpperCase) {
            return target.toUpperCase();
        } else if (isTemplateLowerCase) {
            return target.toLowerCase();
        } else {
            // If template is mixed case, return target as is
            return target;
        }
    }
}
