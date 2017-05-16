# ![RealWorld Example App using Kotlin and Spring](kotlin-spring.png)

> ### Kotlin + Spring codebase containing real world examples (CRUD, auth, advanced patterns, etc) that adheres to the [RealWorld](https://github.com/gothinkster/realworld-example-apps) spec and API.

This codebase was created to demonstrate a fully fledged fullstack application built with Kotlin + Spring including CRUD operations, authentication, routing, pagination, and more.

We've gone to great lengths to adhere to the Kotlin + Spring community styleguides & best practices.

For more information on how to this works with other frontends/backends, head over to the [RealWorld](https://github.com/gothinkster/realworld) repo.

# How it works

The application uses Spring (Web, Data, AOP, Cache) and the Kotlin language.

    + client/
        Some feign clients for testing
    + exception/
        Exceptions by the application
    + jwt/
        AOP advice that check for authentication using a defined @ApiKeySecured operation
    + model/
        + inout/
            Object for REST in/out operations
        JPA models
    + repository/
        + specification/
            Some specifications for JPA
        Spring repositories
    + service/
        Spring services
    + web/
        Spring controllers
    - ApiApplication.kt <- The main class

# Security

Instead of using Spring Security to implement an authenticator using JWT, I created a simple AOP advice that checks
the `Authorization` header and put the user to be found in a `ThreadLocal` (see `UserService`).

The secret key and jwt issuer are stored in `application.properties`.

# Database

It uses a H2 in memory database (for now), can be changed easily in the `application.properties` for any other database.
You'll need to add the correct maven dependency for the needed `Driver` in `pom.xml`.

# Getting started

You need Java and maven installed.

    mvn spring-boot:run
    open http://localhost:8080

# Help

Please fork and PR to improve the code.

# Kotlin

I've been using Kotlin for some time, but I'm no expert, so feel free to contribute and modify the code to make it more idiomatic!
