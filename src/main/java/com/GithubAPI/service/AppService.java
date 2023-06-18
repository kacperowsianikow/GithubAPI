package com.GithubAPI.service;

import com.GithubAPI.exception.UserNotFoundException;
import com.GithubAPI.github.BranchDto;
import com.GithubAPI.github.ResponseDto;
import com.GithubAPI.github.githubresponse.Branch;
import com.GithubAPI.github.githubresponse.Repository;
import com.GithubAPI.mapper.AppMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppService implements IAppService {
    @Value("${github.baseurl}")
    private String githubApiUrl;
    @Value("${github.token}")
    private String token; //TODO: implement token
    private final AppMapper appMapper;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;
    private static final String USER_ERROR_MESSAGE =
            "No repositories found for user with username: ";

    public AppService(AppMapper appMapper,
                      ObjectMapper objectMapper,
                      WebClient.Builder webClientBuilder) {
        this.appMapper = appMapper;
        this.objectMapper = objectMapper;
        this.webClient = webClientBuilder.build();
    }

    @Override
    public List<ResponseDto> getRepositoriesData(String username) {
        String repoUrl = String.format("%s/users/%s/repos",
                githubApiUrl, username);

        return Arrays.stream(
                Objects.requireNonNull(webClient
                                .get()
                                .uri(repoUrl)
                                .retrieve()
                                .onStatus(
                                        HttpStatusCode::is4xxClientError,
                                        clientResponse -> Mono.error(new UserNotFoundException(
                                                USER_ERROR_MESSAGE + username
                                        ))
                                )
                                .bodyToMono(Repository[].class)
                                .log()
                                .flatMapMany(Flux::fromArray)
                                .collectList()
                                .block())
                        .toArray()
                )
                .map(repo -> objectMapper.convertValue(repo, Repository.class))
                .filter(repo -> !repo.fork())
                .map(repo -> appMapper.toResponseDto(
                        repo,
                        getReposBranches(
                                repo.owner().login(),
                                repo.name()
                        )
                ))
                .collect(Collectors.toList());
    }

    private List<BranchDto> getReposBranches(String username,
                                             String repositoryName) {
        String branchUrl = String.format("%s/repos/%s/%s/branches",
                githubApiUrl, username, repositoryName);

        return Arrays.stream(
                Objects.requireNonNull(webClient
                                .get()
                                .uri(branchUrl)
                                .retrieve()
                                .bodyToMono(Branch[].class)
                                .log()
                                .flatMapMany(Flux::fromArray)
                                .collectList()
                                .block())
                        .toArray()
                )
                .map(branch -> objectMapper.convertValue(branch, Branch.class))
                .map(appMapper::toBranchDto)
                .collect(Collectors.toList());
    }

}
