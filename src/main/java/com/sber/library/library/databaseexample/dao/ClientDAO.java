package com.sber.library.library.databaseexample.dao;

import com.sber.library.library.databaseexample.model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private final Connection connection;

    public ClientDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertClientInDB(int id,
                                 String surname,
                                 String name,
                                 String dayOfBirth,
                                 String phone,
                                 String email) throws SQLException {
        PreparedStatement query = connection.prepareStatement("insert into clients (client_id, client_surname, client_name, client_dayofbirth, client_phone, client_email) VALUES (?,?,?,?,?,?)");
        query.setInt(1, id);
        query.setString(2, surname);
        query.setString(3, name);
        query.setString(4, dayOfBirth);
        query.setString(5, phone);
        query.setString(6, email);
        query.executeUpdate();
        Client client = new Client(id, surname, name, dayOfBirth, phone, email);
        System.out.println(client);
    }

    public List<String> booksOfClient(String email) throws SQLException {
        PreparedStatement query = connection.prepareStatement(
                "select book_title from books join clients on client_reader_id=client_id where client_email = ?");
        query.setString(1, email);
        ResultSet resultSet = query.executeQuery();
        List<String> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(resultSet.getString("book_title"));
        }
        return list;
    }
}
