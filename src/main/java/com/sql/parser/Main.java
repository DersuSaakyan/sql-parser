package com.sql.parser;

import com.sql.parser.service.SQLParser;

public class Main {
    public static void main(String[] args) {
        String sql = """
                SELECT author.name as bookName, count(book.id) as bookId, sum(book.cost) as costId
                FROM author b
                left JOIN book ON (author.id = book.author_id)
                INNER JOIN book ON (author.id = book.author_id)
                WHERE book.cost > 100 AND book.name = (SELECT nm.book_name FROM book_name nm WHERE nm.book_id = book.id)
                GROUP BY author.name
                HAVING author.age > 19
                ORDER BY sum(book.cost), book.id
                LIMIT 10 OFFSET 5""";
        System.out.println(SQLParser.parse(sql));
    }
}
