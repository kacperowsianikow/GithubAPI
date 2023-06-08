package com.GithubAPI.exception;

public record ApiException(int status,
                           String message) {

}
