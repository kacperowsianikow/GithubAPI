package com.GithubAPI.controller;

import com.GithubAPI.exception.ApiException;
import com.GithubAPI.service.IAppService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github-repos")
@Slf4j
public class AppController {
    private final IAppService iAppService;
    private final ObjectMapper objectMapper;

    @GetMapping("/list-repos")
    public ResponseEntity<Object> getRepositories(
            @RequestParam("username") String username,
            @RequestHeader(HttpHeaders.ACCEPT) String header) throws JsonProcessingException {
        if (!header.equals(MediaType.APPLICATION_JSON_VALUE)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(objectMapper.writeValueAsString(
                            new ApiException(
                                    HttpStatus.NOT_ACCEPTABLE.value(),
                                    "Provided not acceptable 'Accept' header value: "
                                            + header
                            )
                    ));
        }

        return ResponseEntity.ok(iAppService.getRepositories(username));
    }

}
