package ru.mentee.power.crm.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class LeadControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetLeadsReturns200Ok() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/leads"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("Email")));
    }

    @Test
    public void testGetLeadsReturnsValidHtml() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/leads"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith("text/html"))
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("arasaka@arasaka.com")))
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("QUALIFIED")));
    }

    @Test
    public void testGetLeadsWithNewStatusFilter() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/leads?status=NEW"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("Показаны лиды со статусом: NEW")))
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("NEW")))
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("bg-blue-500")));
    }

    @Test
    public void testGetLeadsWithContactedStatusFilter() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/leads?status=CONTACTED"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("Показаны лиды со статусом: CONTACTED")))
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("CONTACTED")));
    }

    @Test
    public void testGetLeadsWithQualifiedStatusFilter() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/leads?status=QUALIFIED"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("Показаны лиды со статусом: QUALIFIED")))
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("QUALIFIED")));
    }

    @Test
    public void testAllStatusFilterButtonsExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/leads"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("Все")))
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("NEW")))
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("CONTACTED")))
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("QUALIFIED")));
    }
}
