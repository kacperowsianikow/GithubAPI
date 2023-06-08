package com.GithubAPI.github;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class GithubClient {
    @Value("${github.baseurl}")
    private String githubApiUrl;
    @Value("${github.token}")
    private String token;
    private final OkHttpClient okHttpClient = new OkHttpClient();

    public String getRepositories(String username) {
        StringBuilder urlBuilder = new StringBuilder(githubApiUrl);
        urlBuilder
                .append("/users/")
                .append(username)
                .append("/repos");

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();

        try {
            return Objects
                    .requireNonNull(okHttpClient
                            .newCall(request)
                            .execute()
                            .body()
                    )
                    .string();
        } catch (IOException e) {
            log.warn(
                    "Encountered an error while retrieving repository data from GithubAPI: " +
                            e.getMessage()
            );
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getBranches(String username, String repositoryName) {
        StringBuilder urlBuilder = new StringBuilder(githubApiUrl);
        urlBuilder
                .append("/repos/")
                .append(username)
                .append("/")
                .append(repositoryName)
                .append("/branches");

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();

        try {
            return Objects
                    .requireNonNull(okHttpClient
                            .newCall(request)
                            .execute()
                            .body()
                    )
                    .string();
        } catch (IOException e) {
            log.warn(
                    "Encountered an error while converting branch response body to string: " +
                            e.getMessage()
            );
            throw new RuntimeException(e.getMessage());
        }
    }

}
