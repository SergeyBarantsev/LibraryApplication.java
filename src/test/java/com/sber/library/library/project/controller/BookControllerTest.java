package com.sber.library.library.project.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sber.library.library.project.dto.BookDTO;
import com.sber.library.library.project.jwtsecurity.JwtTokenUtil;
import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.model.Genre;
import com.sber.library.library.project.services.userDetails.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Slf4j
public class BookControllerTest {
    @Autowired
    public MockMvc mvc;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    private String token;
    private static final String ROLE_USER_NAME = "Serg056";

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        token = generateToken(ROLE_USER_NAME);
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }

    private String generateToken(String userName) {
        return jwtTokenUtil.generateToken(customUserDetailsService.loadUserByUsername(userName));
    }


    @Test
    public void listAllBooks() throws Exception {
        String result = mvc.perform(
                        get("/rest/books/list")
                                .headers(generateHeaders())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //System.out.println(result);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        List<Book> books = mapper.readValue(result, new TypeReference<List<Book>>() {
        });
        System.out.println(books.size());
    }

    @Test
    public void createUpdateDeleteTestBook() throws Exception {
        //create test book
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Test Title");
        bookDTO.setGenre(Genre.FANTASY);
        bookDTO.setOnlineCopy("Test Online Copy");
        bookDTO.setStoragePlace("Test Storage Place");
        bookDTO.setAmount(50);
        bookDTO.setPublishYear("TEST");


        String response = mvc.perform(
                        post("/rest/books/add")
                                .headers(generateHeaders())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(bookDTO))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Book book = objectMapper.readValue(response, Book.class);

        //update the same book
        BookDTO bookDTOUpd = new BookDTO();
        bookDTOUpd.setTitle("Test Title UPD");
        bookDTOUpd.setGenre(Genre.FANTASY);
        bookDTOUpd.setOnlineCopy("Test Online Copy UPD");
        bookDTOUpd.setStoragePlace("Test Storage PlaceUPD");
        bookDTOUpd.setAmount(100);
        bookDTOUpd.setPublishYear("TEST UPD");

        String responseNew = mvc.perform(
                        put("/rest/books/update")
                                .param("bookId", book.getId().toString())
                                .headers(generateHeaders())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(bookDTOUpd))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        book = objectMapper.readValue(responseNew, Book.class);
        System.out.println(book);


        //delete test book from db
        String deleteResponse = mvc.perform(
                        delete("/rest/books/delete")
                                .param("bookId", book.getId().toString())
                                .headers(generateHeaders())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(deleteResponse);
    }


    public String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


