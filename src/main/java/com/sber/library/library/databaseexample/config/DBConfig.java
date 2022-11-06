//package com.sber.library.library.databaseexample.config;
//
//import com.sber.library.library.databaseexample.dao.BookDAO;
//import com.sber.library.library.databaseexample.dao.ClientDAO;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Scope;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//import static com.sber.library.library.databaseexample.DBConstants.*;
//
//@Configuration
//public class DBConfig {
////    @Bean
////    @Scope("singleton")
////    public Connection connection() throws SQLException {
////        return DriverManager.getConnection(
////                "jdbc:postgresql://" + DB_HOST + ":" + PORT + "/" + DB, USER, PASSWORD);
////    }
//
//    @Bean
//    public BookDAO bookDAO() throws SQLException {
//        return new BookDAO(connection());
//    }
//
//    @Bean
//    @Scope("prototype")
//    public ClientDAO userDAO() throws SQLException {
//        return new ClientDAO(connection());
//    }
//}
