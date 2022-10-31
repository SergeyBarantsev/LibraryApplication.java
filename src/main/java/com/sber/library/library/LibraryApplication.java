package com.sber.library.library;

import com.sber.library.library.database.config.DBConfig;
import com.sber.library.library.database.dao.BookDAO;
import com.sber.library.library.database.dao.ClientDAO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
////import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import com.sber.library.library.database.model.Book;
//import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;


@SpringBootApplication
public class LibraryApplication implements CommandLineRunner {

//    @Autowired
//    private NamedParameterJdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Override
    public void run(String... args) throws SQLException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(DBConfig.class);

        ClientDAO clientDAO = ctx.getBean(ClientDAO.class);
        BookDAO bookDAO = ctx.getBean(BookDAO.class);

        bookDAO.findByTitle(clientDAO.booksOfClient("fsgggrs@mail.ru"));

        //Добавление новых пользователей в БД
//        clientDAO.insertClientInDB(4,"Petrov","Alex", "1997-05-21","+798634546", "example1@bk.ru");
//        clientDAO.insertClientInDB(5,"Vodkin","Stas", "1997-05-11","+798766546", "example2@bk.ru");

        //Небольшая практика с стримами + jdbc.Template
//        List<Book> list = jdbcTemplate.query("select * from books join clients on client_reader_id=client_id where client_email = 'fsgggrs@mail.ru'",
//                (rs, rowNum) -> new Book(
//                        rs.getInt(1),
//                        rs.getString(2),
//                        rs.getString(3),
//                        rs.getTimestamp(4),
//                        rs.getInt(5)
//                ));
//        list.forEach(System.out::println);
    }
}
