package com.GithubAPI.service;

import com.GithubAPI.github.GithubResponseDto;

import java.util.List;

public interface IAppService {
    List<GithubResponseDto> getRepositories(String username);

}
