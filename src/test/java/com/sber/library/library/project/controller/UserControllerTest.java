package com.sber.library.library.project.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sber.library.library.project.dto.RoleDTO;
import com.sber.library.library.project.dto.UserDTO;
import com.sber.library.library.project.jwtsecurity.JwtTokenUtil;
import com.sber.library.library.project.model.User;
import com.sber.library.library.project.repository.RoleRepository;
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
public class UserControllerTest {
    @Autowired
    private RoleRepository roleRepository;
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
    public void listAllUsers() throws Exception {
        String result = mvc.perform(
                        get("/rest/users/list")
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

        List<User> users = mapper.readValue(result, new TypeReference<List<User>>() {
        });
        System.out.println(users.size());
    }

    @Test
    public void createUpdateDeleteTestUser() throws Exception {
        //create test user
        UserDTO userDTO = new UserDTO();
        userDTO.setUserAddress("TEST");
        userDTO.setUserBackUpEmail("TEST");
        userDTO.setUserDateBirth("1997-02-17");
        userDTO.setUserFirstName("TEST");
        userDTO.setUserMiddleName("TEST");
        userDTO.setUserPassword("TEST");
        userDTO.setUserLogin("TEST");
        userDTO.setUserPhone("TEST");
        userDTO.setRole(new RoleDTO(roleRepository.findRoleById(1L)));


        String response = mvc.perform(
                        post("/rest/users/add")
                                .headers(generateHeaders())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(userDTO))
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
        User user = objectMapper.readValue(response, User.class);

        //update the same user
        UserDTO userDTOUpd = new UserDTO();
        userDTOUpd.setUserAddress("TEST UPD");
        userDTOUpd.setUserBackUpEmail("TEST UPD");
        userDTOUpd.setUserDateBirth("1900-02-17");
        userDTOUpd.setUserFirstName("TEST UPD");
        userDTOUpd.setUserMiddleName("TEST UPD");
        userDTO.setUserPassword("TEST");
        userDTOUpd.setUserLogin("TEST UPD");
        userDTOUpd.setUserPhone("TEST UPD");
        userDTOUpd.setRole(new RoleDTO(roleRepository.findRoleById(2L)));

        String responseNew = mvc.perform(
                        put("/rest/users/update")
                                .param("userId", user.getId().toString())
                                .headers(generateHeaders())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(userDTOUpd))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        user = objectMapper.readValue(responseNew, User.class);
        System.out.println(user);


        //delete test user from db
        String deleteResponse = mvc.perform(
                        delete("/rest/users/delete")
                                .param("userId", user.getId().toString())
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


