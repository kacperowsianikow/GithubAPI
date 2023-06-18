package com.GithubAPI.controller;

import com.GithubAPI.github.BranchDto;
import com.GithubAPI.github.ResponseDto;
import com.GithubAPI.service.IAppService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AppController.class)
class AppControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IAppService iAppService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void getRepositoriesData_shouldReturnListSuccessfully() throws Exception {
        String username = "user123";
        List<ResponseDto> response = List.of(
                new ResponseDto(
                        "FirstRepo",
                        username,
                        List.of(
                                new BranchDto(
                                        "firstBranch",
                                        "commitSha"
                                )
                        )
                )
        );

        when(iAppService.getRepositoriesData(username))
                .thenReturn(response);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/api/github-repos/list-repos")
                .param("username", username);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(response)
                ));

        verify(iAppService).getRepositoriesData(username);
    }

    @Test
    void getRepositoriesData_shouldThrowHttpMediaTypeException() throws Exception {
        String username = "user123";

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/api/github-repos/list-repos")
                .param(username, username)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);

        mockMvc.perform(builder)
                .andExpect(status().is4xxClientError());
    }

}