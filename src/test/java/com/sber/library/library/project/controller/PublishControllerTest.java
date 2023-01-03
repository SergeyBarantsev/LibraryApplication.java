package com.sber.library.library.project.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sber.library.library.project.dto.PublishingDTO;
import com.sber.library.library.project.jwtsecurity.JwtTokenUtil;
import com.sber.library.library.project.model.*;
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
public class PublishControllerTest {
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
    public void listAllPublishes() throws Exception {
        String result = mvc.perform(
                        get("/rest/publishing/list")
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

        List<Publishing> publishes = mapper.readValue(result, new TypeReference<List<Publishing>>() {
        });
        System.out.println(publishes.size());
    }

    @Test
    public void createUpdateDeleteTestPublishes() throws Exception {
        //create test publish
        PublishingDTO publishingDTO = new PublishingDTO();
        publishingDTO.setRentDateOutput("TEST");
        publishingDTO.setReturnDateOutput("TEST");
        publishingDTO.setBookTitleSearch("TEST");


        String response = mvc.perform(
                        post("/rest/publishing/add")
                                .headers(generateHeaders())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(publishingDTO))
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
        Publishing publishing = objectMapper.readValue(response, Publishing.class);

        //update the same book
        PublishingDTO publishingDTOUpd = new PublishingDTO();
        publishingDTOUpd.setRentDateOutput("TEST UPD");
        publishingDTOUpd.setReturnDateOutput("TEST UPD");
        publishingDTOUpd.setBookTitleSearch("TEST UPD");


        String responseNew = mvc.perform(
                        put("/rest/publishing/update")
                                .param("publishingId", publishing.getId().toString())
                                .headers(generateHeaders())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(publishingDTOUpd))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        publishing = objectMapper.readValue(responseNew, Publishing.class);
        System.out.println(publishing);


        //delete test book from db
        String deleteResponse = mvc.perform(
                        delete("/rest/publishing/delete")
                                .param("publishingId", publishing.getId().toString())
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


