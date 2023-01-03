package com.sber.library.library.project.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sber.library.library.project.dto.AuthorDTO;
import com.sber.library.library.project.jwtsecurity.JwtTokenUtil;
import com.sber.library.library.project.model.Author;
import com.sber.library.library.project.services.userDetails.CustomUserDetailsService;
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
public class AuthorControllerTest {
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
    public void listAllAuthors() throws Exception {
        String result = mvc.perform(
                        get("/rest/authors/list")
                                .headers(generateHeaders())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
//              .andExpect(jsonPath("$.*", hasSize(5)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        //System.out.println(result);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        List<Author> authors = mapper.readValue(result, new TypeReference<List<Author>>() {
        });
        System.out.println(authors.size());
        //assertEquals(5, authors.size());
        //assertTrue(authors.size() > 0);
    }

    @Test
    public void createUpdateDeleteTestAuthor() throws Exception {
        //create test author
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setAuthorFIO("TESTEROV TEST TESTEROVICH");
        authorDTO.setLifePeriod("2022-2023");
        authorDTO.setDescription("Author for TEST PURPOSE");
        //authorDTO.setId(null);

        String response = mvc.perform(
                        post("/rest/authors/add")
                                .headers(generateHeaders())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(authorDTO))
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
        Author author = objectMapper.readValue(response, Author.class);
        //update the same author
        AuthorDTO authorDTOUpd = new AuthorDTO();
        authorDTOUpd.setAuthorFIO("TESTEROV TEST TESTEROVICH UPDATED");
        authorDTOUpd.setLifePeriod("2022-2023 UPD");
        authorDTOUpd.setDescription("Author for TEST PURPOSE UPD");
        //authorDTO.setId(null);

        String responseNew = mvc.perform(
                        put("/rest/authors/update")
                                .param("authorId", author.getId().toString())
                                .headers(generateHeaders())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(authorDTOUpd))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        author = objectMapper.readValue(responseNew, Author.class);
        System.out.println(author);
        //delete test author from db

        String deleteResponse = mvc.perform(
                        delete("/rest/authors/delete")
                                .param("authorId", author.getId().toString())
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


