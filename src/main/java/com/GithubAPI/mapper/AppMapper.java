package com.GithubAPI.mapper;

import com.GithubAPI.github.BranchDto;
import com.GithubAPI.github.ResponseDto;
import com.GithubAPI.github.githubresponse.Branch;
import com.GithubAPI.github.githubresponse.Repository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppMapper {
    public ResponseDto toResponseDto(Repository repository,
                                     List<BranchDto> branches) {
        return new ResponseDto(
                repository.name(),
                repository.owner().login(),
                branches
        );
    }

    public BranchDto toBranchDto(Branch branch) {
        return new BranchDto(
                branch.name(),
                branch.commit().sha()
        );
    }

}
