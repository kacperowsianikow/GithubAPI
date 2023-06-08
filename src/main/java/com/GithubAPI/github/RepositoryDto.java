package com.GithubAPI.github;

public record RepositoryDto(String repositoryName,
                            String ownerLogin,
                            boolean fork) {

}
