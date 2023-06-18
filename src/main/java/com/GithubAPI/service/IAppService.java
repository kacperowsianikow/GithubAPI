package com.GithubAPI.service;

import com.GithubAPI.github.ResponseDto;

import java.util.List;

public interface IAppService {
    List<ResponseDto> getRepositoriesData(String username);

}
