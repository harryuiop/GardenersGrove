package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@DataJpaTest
@Import(UserService.class)
public class UserServiceIntegrationTest {
    private UserRepository userRepositoryMock;
    private UserService userService;

    User user1;
    User user2;
    List<User> mockRepositoryUsers;

    @BeforeEach
    public void setUp() {
        userRepositoryMock = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepositoryMock);
        assertTrue(true);
        user1 = new User
                ("user1@gmail.com", "John", "Doe", "Password1!", "2000-01-01");
        user2 = new User
                ("user2@gmail.com", "John", "Doe", "Password1!", "2000-01-01");
        mockRepositoryUsers = new ArrayList<>();
        mockRepositoryUsers.add(user1);
        mockRepositoryUsers.add(user2);


    }

    @Test
    public void addUser_allValid_returnCorrectUser() {
        String email = "user@gmail.com";
        String firstName = "John";
        String lname = "Smith";
        String password = "Password123!";
        String dob = "2000-01-01";
        when(userRepositoryMock.save(Mockito.any())).thenReturn(new User(email,
                firstName, lname, password, dob));
        User user = userService.addUsers(new User(email,
                firstName, lname, password, dob));
        Assertions.assertEquals(user.getEmail(), email);
        Assertions.assertEquals(user.getFirstName(), firstName);
        Assertions.assertEquals(user.getLastName(), lname);
        Assertions.assertEquals(user.getPassword(), password);
        Assertions.assertEquals(user.getDob(), dob);
    }

    @Test
    public void addUser_invalidEmail_returnNullUser() {
        String email = "usergmail.com";
        String firstName = "John";
        String lname = "Smith";
        String password = "Password123!";
        String dob = "2000-01-01";
        when(userRepositoryMock.save(Mockito.any())).thenReturn(new User(email,
                firstName, lname, password, dob));
        User user = userService.addUsers(new User(email,
                firstName, lname, password, dob));
        Assertions.assertNull(user);
    }

    @Test
    public void addUser_invalidfirstName_returnNullUser() {
        String email = "user@gmail.com";
        String firstName = "John67";
        String lname = "Smith";
        String password = "Password123!";
        String dob = "2000-01-01";
        when(userRepositoryMock.save(Mockito.any())).thenReturn(new User(email,
                firstName, lname, password, dob));
        User user = userService.addUsers(new User(email,
                firstName, lname, password, dob));
        Assertions.assertNull(user);
    }

    @Test
    public void addUser_invalidLname_returnNullUser() {
        String email = "user@gmail.com";
        String firstName = "John";
        String lname = "Smith;;";
        String password = "Password123!";
        String dob = "2000-01-01";
        when(userRepositoryMock.save(Mockito.any())).thenReturn(new User(email,
                firstName, lname, password, dob));
        User user = userService.addUsers(new User(email,
                firstName, lname, password, dob));
        Assertions.assertNull(user);
    }

    @Test
    public void addUser_invalidPassword_returnNullUser() {
        String email = "user@gmail.com";
        String firstName = "John";
        String lname = "Smith";
        String password = "password1";
        String dob = "2000-01-01";
        when(userRepositoryMock.save(Mockito.any())).thenReturn(new User(email,
                firstName, lname, password, dob));
        User user = userService.addUsers(new User(email,
                firstName, lname, password, dob));
        Assertions.assertNull(user);
    }

    @Test
    public void addUser_invalidDob_returnNullUser() {
        String email = "user@gmail.com";
        String firstName = "John";
        String lname = "Smith";
        String password = "Password123!";
        String dob = LocalDate.now().toString();
        when(userRepositoryMock.save(Mockito.any())).thenReturn(new User(email,
                firstName, lname, password, dob));
        User user = userService.addUsers(new User(email,
                firstName, lname, password, dob));
        Assertions.assertNull(user);
    }
}
