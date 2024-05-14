# SENG302 Team 400 Project

Project's tech stack includes

- [`gradle`](https://gradle.org/),
- [`Spring Boot`](https://spring.io/),
- [`Thymeleaf`](https://www.thymeleaf.org/),
- [`Spring Boot Starter Mail`](https://docs.spring.io/spring-boot/reference/io/email.html),
- [`MapTiler`](https://www.maptiler.com/cloud/geocoding/),
- [`Open-Meteo`](https://open-meteo.com/)
- [`GitLab CI`](https://about.gitlab.com/).

## How to run tests

> Run ./gradlew test

## How to run

### 1 - Product Status
Our project has not been deployed to PROD instance.

### 2 - Running the program
On Linux:
```
./gradlew bootRun
```

On Windows:
```
gradlew bootRun
```

By default, the application will run on local port 8080 [http://localhost:8080](http://localhost:8080)

### 3 - Using the application

> - You will first need to register an account or login with persisting account.
> - If registering you must get a code from your email to verify your account before you are able to access the account.
> - Once logged in, you have the ability to view the users details, add a profile picture, and change any of the users details.
> - To view the users profile click on the profile image or name. 
> - From viewing the users profile you can edit the profile image and view all public knowledge details (everything except users password).
> - On the edit profile page you can edit users name, email, and date of birth. To edit password you must go through to change password.
> - If password is updated an email will be sent to the user notifying them of the update.
> - When logged in you will also have access to add garden's or view existing gardens that the user has created.
> - All gardens can be seen in the sidebar or on the "My Gardens" page also accessible through the sidebar.
> - When viewing a garden you can also see any plants added to the garden. If you have more than 10 plants you may not be able to see them all on your page, so a scroll wheel appears and you can scroll through them.
> - You can add a plant by viewing the garden you want to add a plant to.
> - You can add an image to the plant either through the adding/editing of a plant or on the garden view page by clicking on the image.

## Default User Credentials
__Email__: user1@gmail.com \
__Password__: Password1!\
User 1 has 1 garden with 10 plants

__Email__: user2@gmail.com \
__Password__: Password1!\
User 2 has 1 garden with no plants

## Contributors

- SENG302 teaching team
- Alexandra Belcher
- Joshua Winter
- Harrison Parkes
- Zoe Perry
- Harry Ellis
- Sam Willems
- Vincent Chen 
- HanByeol Yang

## References

- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring JPA docs](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Thymeleaf Docs](https://www.thymeleaf.org/documentation.html)
- [Learn resources](https://learn.canterbury.ac.nz/course/view.php?id=17797&section=8)
