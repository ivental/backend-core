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
                        org.hamcrest.Matchers.containsString("<!DOCTYPE html>")))
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("<title>")))
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("</html>")));
    }
}
