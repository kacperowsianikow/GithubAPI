package com.GithubAPI.service;

import com.GithubAPI.exception.UserNotFoundException;
import com.GithubAPI.github.BranchDto;
import com.GithubAPI.github.GithubClient;
import com.GithubAPI.github.GithubResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppService implements IAppService {
    private final GithubClient githubClient;

    @Override
    public List<GithubResponseDto> getRepositories(String username) {
        try {
            String repositoryResponse = githubClient.getRepositories(username);
            JSONArray repositoryArray = new JSONArray(repositoryResponse);

            log.info("Retrieved repositories for user: " + username);

            List<GithubResponseDto> response = new ArrayList<>();

            repositoryArray.forEach(repositoryObject -> {
                JSONObject jsonRepositoryObject = (JSONObject) repositoryObject;

                if (!jsonRepositoryObject.getBoolean("fork")) {
                    String repositoryName = jsonRepositoryObject.getString("name");

                    response.add(new GithubResponseDto(
                            repositoryName,
                            jsonRepositoryObject.getJSONObject("owner").getString("login"),
                            getBranches(username, repositoryName)
                    ));
                }
            });

            return response;
        } catch (JSONException e) {
            log.warn(
                    "Encountered error while retrieving repository data: " +
                    e.getMessage()
            );
            throw new UserNotFoundException(
                    "No repositories found for user with username: " + username
            );
        }
    }

    private List<BranchDto> getBranches(String username, String repositoryName) {
        try {
            String branchResponse = githubClient.getBranches(username, repositoryName);
            JSONArray branchArray = new JSONArray(branchResponse);

            log.info("Retrieved branches for repository: " + repositoryName);

            List<BranchDto> branchDtos = new ArrayList<>();

            branchArray.forEach(branchObject -> {
                JSONObject jsonBranchObject = (JSONObject) branchObject;
                branchDtos.add(new BranchDto(
                        jsonBranchObject.getString("name"),
                        jsonBranchObject.getJSONObject("commit").getString("sha")
                ));
            });

            return branchDtos;

        } catch (JSONException e) {
            log.warn("Encountered error while retrieving branch data: " +
                    e.getMessage());
            throw new JSONException(e.getMessage());
        }
    }

}
