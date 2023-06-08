# GithubAPI

Application for retrieving repository data for a given username.

## Used technologies:
- Java 17,
- Spring Boot 3.1.0,
- Lombok,
- OkHttp,
- JSON In Java.

## Setup:
### Generating GitHub token
#### If you already have your token, you can skip this step. Just make sure that your token has 'repo' in scopes
You can do this by following these steps:
- enter your account's settings,
- go to the 'Developer settings',
- in the 'Personal access tokens' select 'Token (classic)',
- then 'Generate new token (classic)',
- in the note field enter name for your token,
- in 'Select scopes' check the box 'repo' with its sub-boxes,
- click generate.

### Entering your token
In application.properties file, in line:
'github.token=YOUR_GITHUB_TOKEN', in place of YOUR_GITHUB_TOKEN you 
should enter token generated in your GitHub account.

### Start up
If you entered generated token into application.properties file, you can 
start up project in your IDE. To perform a request:
- open client app of your choice (like Postman), 
- enter url: localhost:8080/api/github-repos/list-repos,
- add 'Accept' header with value 'application/json', 
- add parameter 'username' with username of User that data you want to retrieve.

## Features:
- For correct input data (username is required and "Accept" header
value "application/json") the response have following format:
    [
        {
            "repositoryName": "RetrievedRepositoryName",
            "ownerLogin": "providedUsername",
                "branches": [
                {
                    "branchName": "sampleBranchName",
                    "lastCommitSha": "sampleSha"
                }
            ]
        }, ...
    ]

- When user is not found you should get the following response:
    {
        "status": 404,
        "message": "No repositories found for user with username: providedUsername"
    }

- When you provide "Accept" header with value "application/mxl" you should 
get the following response:
    {
        "status": 406,
        "message": "Provided not acceptable 'Accept' header value: application/xml"
    }
