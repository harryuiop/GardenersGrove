# SENG302 Team 400 Project

Project's tech stack includes

- [`gradle`](https://gradle.org/),
- [`Spring Boot`](https://spring.io/),
- [`Thymeleaf`](https://www.thymeleaf.org/),
- [`Spring Boot Starter Mail`](https://docs.spring.io/spring-boot/reference/io/email.html),
- [`MapTiler`](https://www.maptiler.com/cloud/geocoding/),
- [`Open-Meteo`](https://open-meteo.com/),
- [`Chart.js`](https://www.chartjs.org/),
- [`GitLab CI`](https://about.gitlab.com/).

## How to run tests

> Run ./gradlew test

## How to run

### 1 - Product Status

Our project has been deployed to PROD instance.

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
> - If you have forgotten your password you can get a reset password form sent to your email from the login page.
> - The forgotten password and register links will expire after 10 minutes.
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
> - You can also add tags to your garden while viewing the garden by entering the tag name and hitting the plus button.
> - Gardens can also be made either public or private to prevent other users from viewing them.
> - To view your friends you must select the My Friends button on the navigation bar. This will take you to the manage
    my friends page.
> - Friends can be removed and friend requests can be sent, accepted or declined on this page.
> - To send a friend request you must search for another users name or email and then select Send Request.
> - Pending and declined requests are visible below the incoming friend requests and can be cancelled on ths page.

## Default User Credentials

### On Test
__Email__: user1@gmail.com \
__Password__: Password1!\
User 1 should have at least 9 gardens, the first with 10 plants and the second with 1 plant.

The first garden contains inserted arduino data for the sensors (on the monitor gardens page).
Dummy sensor data is from August 11th 2024 to October 10th 2024. Note not every 30-minute
period in this time frame is accounted for. Current reading also grabs the newest data reading,
since we need to add data in the future for your marking this result may not be accurate with the graphs.

There are 9 more users that share a password with User 1 and have emails in the same format as User 1 but with a
different number up to 9.

### On Prod

__Email__: user2@gmail.com \
__Password__: Password1!\
User 2 should have at least 10 gardens, the first with 7 plants.

The first garden contains inserted arduino data for the sensors (on the monitor gardens page).
Dummy sensor data is from August 11th 2024 to October 10th 2024. Note not every 30-minute
period in this time frame is accounted for. Current reading also grabs the newest data reading,
since we need to add data in the future for your marking this result may not be accurate with the graphs.

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
- [Bootstrap](https://getbootstrap.com/docs/5.3/getting-started/introduction/)
