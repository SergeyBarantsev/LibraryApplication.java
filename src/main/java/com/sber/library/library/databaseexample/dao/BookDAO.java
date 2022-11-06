package com.sber.library.library.databaseexample.dao;

import com.sber.library.library.databaseexample.model.Book;

import java.sql.*;
import java.util.List;

public class BookDAO {
    private final Connection connection;

    public BookDAO(Connection connection) {
        this.connection = connection;
    }

    public Book findById(final Integer id) throws SQLException {
        PreparedStatement query = connection.prepareStatement("select * from books where book_id = ?");
        query.setInt(1, id);
        ResultSet resultSet = query.executeQuery();
        Book book = new Book();
        while (resultSet.next()) {
            book.setBookId(resultSet.getInt("book_id"));
            book.setBookTitle(resultSet.getString("book_title"));
            book.setBookAuthor(resultSet.getString("book_author"));
            book.setBookDateAdded(resultSet.getTimestamp("book_date_added"));
            System.out.println(book);
        }
        return book;
    }

    public void findByTitle(List<String> list) throws SQLException, RuntimeException {
        PreparedStatement query = connection.prepareStatement("select * from books where book_title = ?");
        if (!list.isEmpty()) {
            for (String str : list) {
                query.setString(1, str);
                ResultSet resultSet = query.executeQuery();
                Book book = new Book();
                while (resultSet.next()) {
                    book.setBookId(resultSet.getInt("book_id"));
                    book.setBookTitle(resultSet.getString("book_title"));
                    book.setBookAuthor(resultSet.getString("book_author"));
                    book.setBookDateAdded(resultSet.getTimestamp("book_date_added"));
                    book.setClientReaderId(resultSet.getInt("client_reader_id"));
                    System.out.println(book);
                }
            }
        } else throw new RuntimeException("У данного клиента нет книг!");
    }
}
