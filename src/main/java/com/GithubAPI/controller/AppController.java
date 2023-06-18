package com.GithubAPI.controller;

import com.GithubAPI.github.ResponseDto;
import com.GithubAPI.service.IAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github-repos")
public class AppController {
    private final IAppService iAppService;

    @GetMapping(value = "/list-repos", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResponseDto> getRepositoriesData(
            @RequestParam("username") String username) {

        return iAppService.getRepositoriesData(username);
    }

}
