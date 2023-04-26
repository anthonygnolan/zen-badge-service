package com.coderdojo.zen.badge;

import com.coderdojo.zen.badge.model.Badge;
import com.coderdojo.zen.badge.model.Category;
import com.coderdojo.zen.badge.repositories.BadgeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BadgeITTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        badgeRepository.deleteAll();
    }

    @Test
    @Order(1)
    void givenBadgeObject_whenCreateBadge_thenReturnSavedBadge() throws Exception{

        // given - precondition or setup
        Badge badge = new Badge(
            null,
            "Scratch 2",
            "This is a Scratch badge.",
            Category.PROGRAMMING
        );

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/v1/badges")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badge)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.name",
                        is(badge.getName())))
                .andExpect(jsonPath("$.description",
                        is(badge.getDescription())))
                .andExpect(jsonPath("$.category",
                        is(badge.getCategory().toString())));

    }

    // JUnit test for Get All badges REST API
    @Test
    @Order(2)
    void givenListOfBadges_whenGetAllBadges_thenReturnBadgesList() throws Exception{
        // given - precondition or setup
        List<Badge> listOfBadges = new ArrayList<>();
        listOfBadges.add(new Badge(1L,"MacBook Pro", "", Category.PROGRAMMING));
        listOfBadges.add(new Badge(2L,"iPhone", "", Category.PROGRAMMING));
        badgeRepository.saveAll(listOfBadges);
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/badges"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfBadges.size())));

    }

    @Test
    @Order(3)
    void givenListOfBadges_whenGetBadgesByCategory_thenReturnBadgesForCategory() throws Exception{
        // given - precondition or setup
        List<Badge> listOfBadges = new ArrayList<>();
        listOfBadges.add(new Badge(null,"One", "one", Category.SOFT_SKILLS));
        listOfBadges.add(new Badge(null,"Two", "two", Category.SOFT_SKILLS));
        badgeRepository.saveAll(listOfBadges);
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/badges")
                .param("category", "soft_skills"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfBadges.size())));

    }

    @Test
    @Order(4)
    void givenListOfBadges_whenGetBadgesByInvalidCategory_thenReturnAllBadges() throws Exception{
        // given - precondition or setup
        List<Badge> listOfBadges = new ArrayList<>();
        listOfBadges.add(new Badge(null,"One", "one", Category.PROGRAMMING));
        listOfBadges.add(new Badge(null,"Two", "two", Category.PROGRAMMING));
        badgeRepository.saveAll(listOfBadges);
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/badges")
                .param("category", "fake"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfBadges.size())));

    }

    // positive scenario - valid badge id
    // JUnit test for GET badge by id REST API
    @Test
    @Order(5)
    void givenBadgeId_whenGetBadgeById_thenReturnBadgeObject() throws Exception{
        // given - precondition or setup
        Badge badge = badgeRepository.save(
                new Badge(null,"JavaScript", "This is a JavaScript badge", Category.PROGRAMMING)
        );
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/badges/{id}", badge.getId()));
        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.description",
                        is(badge.getDescription())))
                .andExpect(jsonPath("$.category",
                        is(badge.getCategory().toString())));

    }

    // negative scenario - valid badge id
    // JUnit test for GET badge by id REST API
    @Test
    @Order(6)
    void givenInvalidBadgeId_whenGetBadgeById_thenReturnEmpty() throws Exception{

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/badges/{id}", 11L));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    // JUnit test for update badge REST API - positive scenario
    @Test
    @Order(7)
    void givenUpdatedBadge_whenUpdateBadge_thenReturnUpdateBadgeObject() throws Exception{
        // given - precondition or setup
        Badge badge = badgeRepository.save(
                new Badge(null,
                        "Python",
                        "This is a Javascript badge",
                        Category.PROGRAMMING
                )
        );
        Badge updatedBadge = new Badge(badge.getId(),"Python", "This is a Python badge", Category.PROGRAMMING);
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/v1/badges/{id}", badge.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Python\",\"description\":\"This is a Python badge\",\"category\":\"PROGRAMMING\"}"));


        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.description",
                        is(updatedBadge.getDescription())))
                .andExpect(jsonPath("$.category",
                        is(updatedBadge.getCategory().toString())));
    }

    // JUnit test for update badge REST API - negative scenario
    @Test
    @Order(8)
    void givenUpdatedBadge_whenUpdateBadge_thenReturn404() throws Exception{
        // given - precondition or setup

        Badge badge = new Badge(
                15L,
                "Swift",
                "This is a swift badge",
                Category.PROGRAMMING
        );

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/v1/badges/{id}", badge.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badge)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit test for delete badge REST API
    @Test
    @Order(9)
    void givenBadgeId_whenDeleteBadge_thenReturn200() throws Exception{
        // given - precondition or setup
        Badge badge = badgeRepository.save(
                new Badge(
                        null,
                        "Python",
                        "This is a Javascript badge",
                        Category.PROGRAMMING
                )
        );

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/v1/badges/{id}", badge.getId()));

        // then - verify the output
        response.andExpect(status().isNoContent())
                .andDo(print());
    }
}