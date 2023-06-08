package com.GithubAPI.github;

import java.util.List;

public record GithubResponseDto(String repositoryName,
                                String ownerLogin,
                                List<BranchDto> branches) {

}
