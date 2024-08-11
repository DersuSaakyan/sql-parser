package com.sql.parser;

import com.sql.parser.exception.RequiredClauseMissingException;
import com.sql.parser.service.SQLParser;
import com.sql.parser.sqlclause.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SQLParserTest {

    @Test
    public void testCorrectSql_SelectPart() {
        final String sql = """
                SELECT author.name as bookName, count(book.id) as bookId, sum(book.cost) as costSum
                FROM author b
                LEFT JOIN book ON (author.id = book.author_id)
                INNER JOIN book ON (author.id = book.author_id)
                WHERE book.cost > 100 AND book.name = (SELECT nm.book_name FROM book_name nm WHERE nm.book_id = book.id)
                GROUP BY author.name
                ORDER BY sum(book.cost), book.id""";

        final QueryResponse parse = SQLParser.parse(sql);

        assertNotNull(parse);
        final SelectClause select = parse.getSelect();

        assertNotNull(select);

        final List<SelectClause.Select> columns = select.columns();
        assertEquals("author.name", columns.get(0).name());
        assertEquals("bookName", columns.get(0).alias());

        assertEquals("count(book.id)", columns.get(1).name());
        assertEquals("bookId", columns.get(1).alias());

        assertEquals("sum(book.cost)", columns.get(2).name());
        assertEquals("costSum", columns.get(2).alias());
    }

    @Test
    public void testCorrectSql_FromPart() {
        final String sql = """
                SELECT author.name as bookName, count(book.id) as bookId, sum(book.cost) as costSum
                FROM author a, book b
                left JOIN book ON (author.id = book.author_id)
                INNER JOIN book ON (author.id = book.author_id)
                WHERE book.cost > 100 AND book.name = (SELECT nm.book_name FROM book_name nm WHERE nm.book_id = book.id)
                GROUP BY author.name
                HAVING author.age > 19
                ORDER BY sum(book.cost), book.id
                LIMIT 10 OFFSET 5""";

        final QueryResponse parse = SQLParser.parse(sql);

        assertNotNull(parse);
        final FromClause from = parse.getFrom();

        assertNotNull(from);

        final List<FromClause.From> froms = from.froms();

        assertEquals("author", froms.get(0).tableName());
        assertEquals("a", froms.get(0).alias());

        assertEquals("book", froms.get(1).tableName());
        assertEquals("b", froms.get(1).alias());
    }

    @Test
    public void testCorrectSql_JoinPart() {
        final String sql = """
                SELECT author.name as bookName, count(book.id) as bookId, sum(book.cost) as costSum
                FROM author a, book b
                left JOIN book ON (author.id = book.author_id)
                INNER JOIN book ON (author.id = book.author_id)
                WHERE book.cost > 100 AND book.name = (SELECT nm.book_name FROM book_name nm WHERE nm.book_id = book.id)
                GROUP BY author.name
                HAVING author.age > 19
                ORDER BY sum(book.cost), book.id
                LIMIT 10 OFFSET 5""";

        final QueryResponse parse = SQLParser.parse(sql);

        assertNotNull(parse);
        final JoinClause join = parse.getJoin();

        assertNotNull(join);

        final List<JoinClause.Join> joins = join.joins();

        assertEquals("left", joins.get(0).type());
        assertEquals("book ON (author.id = book.author_id)", joins.get(0).tableWithCondition());

        assertEquals("INNER", joins.get(1).type());
        assertEquals("book ON (author.id = book.author_id)", joins.get(1).tableWithCondition());
    }

    @Test
    public void testCorrectSql_WHEREPart() {
        final String sql = """
                SELECT author.name as bookName, count(book.id) as bookId, sum(book.cost) as costSum
                FROM author a, book b
                left JOIN book ON (author.id = book.author_id)
                INNER JOIN book ON (author.id = book.author_id)
                WHERE book.cost > 100 AND book.name = (SELECT nm.book_name FROM book_name nm WHERE nm.book_id = book.id)
                GROUP BY author.name
                HAVING author.age > 19
                ORDER BY sum(book.cost), book.id
                LIMIT 10 OFFSET 5""";

        final QueryResponse parse = SQLParser.parse(sql);

        assertNotNull(parse);
        final WhereClause whereClause = parse.getWhere();

        assertNotNull(whereClause);

        assertEquals("book.cost > 100 AND book.name = (SELECT nm.book_name FROM book_name nm WHERE nm.book_id = book.id)", whereClause.condition());
    }

    @Test
    public void testCorrectSql_GROUPBYPart() {
        final String sql = """
                SELECT author.name as bookName, count(book.id) as bookId, sum(book.cost) as costSum
                FROM author a, book b
                left JOIN book ON (author.id = book.author_id)
                INNER JOIN book ON (author.id = book.author_id)
                WHERE book.cost > 100 AND book.name = (SELECT nm.book_name FROM book_name nm WHERE nm.book_id = book.id)
                GROUP BY author.name
                ORDER BY sum(book.cost), book.id""";

        final QueryResponse parse = SQLParser.parse(sql);

        assertNotNull(parse);
        final GroupByClause groupByClause = parse.getGroupBy();

        assertNotNull(groupByClause);

        assertEquals("author.name", groupByClause.name());
    }

    @Test
    public void testCorrectSql_HavingPart() {
        final String sql = """
                SELECT author.name as bookName, count(book.id) as bookId, sum(book.cost) as costSum
                FROM author a, book b
                WHERE book.cost > 100 AND book.name = (SELECT nm.book_name FROM book_name nm WHERE nm.book_id = book.id)
                GROUP BY author.name
                HAVING author.age > 19
                ORDER BY sum(book.cost), book.id
                LIMIT 10 OFFSET 5""";

        final QueryResponse parse = SQLParser.parse(sql);

        assertNotNull(parse);
        final HavingClause havingClause = parse.getHaving();

        assertNotNull(havingClause);

        assertEquals("author.age > 19", havingClause.condition());
    }


    @Test
    public void testCorrectSql_OrderByPart() {
        final String sql = """
                SELECT author.name as bookName, count(book.id) as bookId, sum(book.cost) as costSum
                FROM author a, book b
                left JOIN book ON (author.id = book.author_id)
                INNER JOIN book ON (author.id = book.author_id)
                GROUP BY author.name
                HAVING author.age > 19
                ORDER BY sum(book.cost), book.id
                LIMIT 10 OFFSET 5""";

        final QueryResponse parse = SQLParser.parse(sql);

        assertNotNull(parse);
        final OrderByClause orderClause = parse.getOrderBy();

        assertNotNull(orderClause);

        final List<OrderByClause.Order> orders = orderClause.orders();
        assertEquals("ASC", orderClause.direction());
        assertEquals("sum(book.cost)", orders.get(0).column());
        assertEquals("book.id", orders.get(1).column());
    }

    @Test
    public void testCorrectSql_LimitByPart() {
        final String sql = """
                SELECT author.name as bookName, count(book.id) as bookId, sum(book.cost) as costSum
                FROM author a, book b
                left JOIN book ON (author.id = book.author_id)
                INNER JOIN book ON (author.id = book.author_id)
                WHERE book.cost > 100 AND book.name = (SELECT nm.book_name FROM book_name nm WHERE nm.book_id = book.id)
                GROUP BY author.name
                HAVING author.age > 19
                ORDER BY sum(book.cost), book.id
                LIMIT 10""";

        final QueryResponse parse = SQLParser.parse(sql);

        assertNotNull(parse);
        final LimitClause limitClause = parse.getLimit();

        assertNotNull(limitClause);

        assertEquals("10", limitClause.limit());
    }

    @Test
    public void testCorrectSql_OffsetByPart() {
        final String sql = """
                SELECT author.name as bookName, count(book.id) as bookId, sum(book.cost) as costSum
                FROM author a, book b
                left JOIN book ON (author.id = book.author_id)
                INNER JOIN book ON (author.id = book.author_id)
                WHERE book.cost > 100 AND book.name = (SELECT nm.book_name FROM book_name nm WHERE nm.book_id = book.id)
                GROUP BY author.name
                HAVING author.age > 19
                ORDER BY sum(book.cost), book.id
                LIMIT 10 OFFSET 5""";

        final QueryResponse parse = SQLParser.parse(sql);

        assertNotNull(parse);
        final OffsetClause offsetClause = parse.getOffset();

        assertNotNull(offsetClause);

        assertEquals("5", offsetClause.offset());
    }

    @Test
    public void simpleQuery_WithOrderNameRepeated() {
        final String sql = """
                SELECT o.name as orderName
                FROM order o
                order by o.id""";

        final QueryResponse parse = SQLParser.parse(sql);

        assertNotNull(parse);
        final SelectClause selectClause = parse.getSelect();

        assertNotNull(selectClause);
        assertEquals("o.name", selectClause.columns().get(0).name());
        assertEquals("orderName", selectClause.columns().get(0).alias());

        final FromClause fromClause = parse.getFrom();
        assertNotNull(fromClause);
        assertEquals("order", fromClause.froms().get(0).tableName());
        assertEquals("o", fromClause.froms().get(0).alias());

        final OrderByClause orderByClause = parse.getOrderBy();
        assertNotNull(orderByClause);
        assertEquals("o.id", orderByClause.orders().get(0).column());
        assertEquals("ASC", orderByClause.direction());
    }

    @Test
    public void throwExceptionWhenMissingSelectMissingPart() {
        final String sql = """
                FROM order o
                order by o.id""";

        assertThrows(RequiredClauseMissingException.class, () -> SQLParser.parse(sql));
    }

    @Test
    public void throwExceptionWhenMissingFromMissingPart() {
        final String sql = """
                SELECT o.id order o
                order by o.id""";

        assertThrows(RequiredClauseMissingException.class, () -> SQLParser.parse(sql));
    }
}
