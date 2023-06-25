package com.GithubAPI.github.githubresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Repository(String name,
                         Owner owner,
                         boolean fork) {

}
