package com.GithubAPI.github;

import java.util.List;

public record ResponseDto(String repositoryName,
                          String ownerLogin,
                          List<BranchDto> branches) {

}
