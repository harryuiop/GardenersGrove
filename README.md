# Gardeners Grove

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)

## Overview

Gardeners Grove is a web application that helps users manage their gardens and plants. The application includes features for user authentication, garden management, plant tracking, friend connections, and sensor data monitoring.

## Dependencies

- [MapTiler](https://www.maptiler.com/cloud/geocoding/) - Geocoding services
- [Open-Meteo](https://open-meteo.com/) - Weather data
- [Chart.js](https://www.chartjs.org/) - Data visualization

## Getting Started

### How to Run Tests

```bash
./gradlew test
```

### How to Run the Application

**On Linux:**
```bash
./gradlew bootRun
```

**On Windows:**
```bash
gradlew bootRun
```

By default, the application runs on [http://localhost:8080](http://localhost:8080)

## Using the Application

### Authentication

- **Registration**: Create a new account and verify using the email code sent to you
- **Login**: Access your account with email and password
- **Password Reset**: Request a password reset form via email from the login page
- **Note**: Registration and password reset links expire after 10 minutes

### User Profile

- View user details by clicking on the profile image or name
- Add or change profile picture
- Edit user details (name, email, date of birth)
- Change password (generates email notification)

### Garden Management

- Access gardens through the sidebar or "My Gardens" page
- View existing gardens created by the user
- Add new gardens to your account
- Set gardens as public or private to control visibility

### Sensor Data

- Monitor garden sensor data on the "Monitor Gardens" page
- View historical sensor data visualized in graphs
- Check current sensor readings

### Plant Management

- View plants associated with each garden
- Scroll through plants when there are more than 10
- Add plants to specific gardens
- Add images to plants during creation/editing or from the garden view
- Add tags to gardens for better organization

### Friend Management

- Access friend list via "My Friends" in the navigation bar
- Send friend requests by searching for users by name or email
- Accept or decline incoming friend requests
- View pending and declined requests
- Cancel sent requests

## Default User Credentials

### Test Environment

**Email**: user1@gmail.com  
**Password**: Password1!

Features:
- Has at least 9 gardens
- First garden has 10 plants
- Second garden has 1 plant
- First garden contains Arduino sensor data (August 11th to October 10th, 2024)

Additional test users available with emails user2@gmail.com through user9@gmail.com (same password)

### Production Environment

**Email**: user2@gmail.com  
**Password**: Password1!

Features:
- Has at least 10 gardens
- First garden has 7 plants
- First garden contains Arduino sensor data (August 11th to October 10th, 2024)

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

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring JPA Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [University of Canterbury Learn Resources](https://learn.canterbury.ac.nz/course/view.php?id=17797&section=8)
- [Bootstrap Documentation](https://getbootstrap.com/docs/5.3/getting-started/introduction/)
