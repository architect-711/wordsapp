# Documentation

## Description
In this file you'll find:
1. Where to find the API config files
2. The commits styles
3. Info about test
4. The project strcutre logic

## API
In this folder there are some dedicated files: the `Yaak.wordsapp.json` (because
the Postman is not my choose) and the `api-docs.json` (the OpenAPI config)

The `v1.0.0` version uses the Base64 security for simplisity and will be replaced
with minimum afforts in future versions.

## The commits style
Use only the style described in the [conventional commits site](https://www.conventionalcommits.org/en/v1.0.0/#summary)

## Tests
This is the most crucial part of the app. Here, I keep the one simple rule:
"No test - not feature".

### Integrational tests
I've tried many approaches but stopped on the one: **each test is independent
and standalone and must leave nothing after it**!!! It must every single time in the beginning save what it need
and at the end clean everything. <u>In some cases you can reject of this pratice, but
make sure it's justified. Because some tests might require the database to be empty!</u>

To reach that the `@AfterAll` and `@BeforeAll` methods exist. They might, for example,
clear the `SecurityContextHolder` for you.

## Project Structure
The logic is simple: each feature has it's own dedicated directory. Follow the logic:
* It's a controller? - put to the `/controller` folder. 
* It's a service - put to the `/service` folder
* It's a service util? - put to the `/service/utils` folder
* It maps DTOs, POJOs or Entities? - `/model/mapper` folder is for you and etc.

So the FS should look something like this (this is just an **example**):
```
src/main/java/edu/architect_711/wordsapp/                   - the root of project
├── config                                                  - contains @Configuration's
│   ├── OpenAPIConfiguration.java
│   └── SecurityConfiguration.java
├── controller                                              - controllers
│   ├── AccountController.java
│   ├── ExceptionController.java
├── exception                                               - cutsom exceptions
│   └── UnauthorizedGroupModifyAttemptException.java        
├── generator                                               - generators
│   └── Base64Generator.java
├── model                                                   - DTO's, mappers, @Entities, POJOs
│   ├── dto                                                 - DTO
│   │   ├── account                                         - DTO domain
│   │   │   ├── AccountDto.java
│   │   │   ├── AccountLoginRequest.java
│   │   ├── group
│   │   │   ├── GroupDto.java
│   │   │   ├── SaveGroupDto.java
│   ├── entity                                              - @Entity
│   │   ├── Account.java
│   │   ├── Language.java
│   └── mapper                                              - mapper
│       ├── AccountMapper.java
│       └── WordMapper.java
├── repository                                              - Database repositories
│   ├── AccountRepository.java
│   └── WordRepository.java
├── security                                                - security-related
│   ├── service                                             - security services
│   │   └── AccountDetailsService.java
│   └── utils                                               - utils
│       ├── AuthenticationExtractor.java
│       └── CheckAccess.java
├── service                                                 - services 
│   ├── account                                             - domain of service
│   │   ├── AccountService.java                             - interface
│   │   └── DefaultAccountService.java                      - implementation
│   ├── group
│   │   ├── DefaultGroupService.java
│   │   └── GroupService.java
└── WordsAppApplication.java                                - main file
```