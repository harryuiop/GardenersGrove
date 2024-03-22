package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Users;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@DataJpaTest
@Import(UserService.class)
public class UserServiceIntegrationTest {
    private UserRepository userRepositoryMock;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepositoryMock = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepositoryMock);
        assertTrue(true);
    }

    @Test
    public void addUser_allValid_returnCorrectUser() {
        String email = "user@gmail.com";
        String fname = "John";
        String lname = "Smith";
        String password = "Password123!";
        String address = "1 Test Road";
        String dob = "2000-01-01";
        when(userRepositoryMock.save(Mockito.any())).thenReturn(new Users(email,
                fname, lname, address, password, dob));
        Users user = userService.addUsers(new Users(email,
                fname, lname, address, password, dob));
        Assertions.assertEquals(user.getEmail(), email);
        Assertions.assertEquals(user.getFname(), fname);
        Assertions.assertEquals(user.getLname(), lname);
        Assertions.assertEquals(user.getPassword(), password);
        Assertions.assertEquals(user.getAddress(), address);
        Assertions.assertEquals(user.getDob(), dob);
    }

    @Test
    public void addUser_invalidEmail_returnNullUser() {
        String email = "usergmail.com";
        String fname = "John";
        String lname = "Smith";
        String password = "Password123!";
        String address = "1 Test Road";
        String dob = "2000-01-01";
        when(userRepositoryMock.save(Mockito.any())).thenReturn(new Users(email,
                fname, lname, address, password, dob));
        Users user = userService.addUsers(new Users(email,
                fname, lname, address, password, dob));
        Assertions.assertNull(user);
    }

    @Test
    public void addUser_invalidFname_returnNullUser() {
        String email = "user@gmail.com";
        String fname = "John67";
        String lname = "Smith";
        String password = "Password123!";
        String address = "1 Test Road";
        String dob = "2000-01-01";
        when(userRepositoryMock.save(Mockito.any())).thenReturn(new Users(email,
                fname, lname, address, password, dob));
        Users user = userService.addUsers(new Users(email,
                fname, lname, address, password, dob));
        Assertions.assertNull(user);
    }

    @Test
    public void addUser_invalidLname_returnNullUser() {
        String email = "user@gmail.com";
        String fname = "John";
        String lname = "Smith;;";
        String password = "Password123!";
        String address = "1 Test Road";
        String dob = "2000-01-01";
        when(userRepositoryMock.save(Mockito.any())).thenReturn(new Users(email,
                fname, lname, address, password, dob));
        Users user = userService.addUsers(new Users(email,
                fname, lname, address, password, dob));
        Assertions.assertNull(user);
    }

    @Test
    public void addUser_invalidPassword_returnNullUser() {
        String email = "user@gmail.com";
        String fname = "John";
        String lname = "Smith";
        String password = "password1";
        String address = "1 Test Road";
        String dob = "2000-01-01";
        when(userRepositoryMock.save(Mockito.any())).thenReturn(new Users(email,
                fname, lname, address, password, dob));
        Users user = userService.addUsers(new Users(email,
                fname, lname, address, password, dob));
        Assertions.assertNull(user);
    }

    @Test
    public void addUser_invalidDob_returnNullUser() {
        String email = "user@gmail.com";
        String fname = "John";
        String lname = "Smith";
        String password = "Password123!";
        String address = "1 Test Road";
        String dob = LocalDate.now().toString();
        when(userRepositoryMock.save(Mockito.any())).thenReturn(new Users(email,
                fname, lname, address, password, dob));
        Users user = userService.addUsers(new Users(email,
                fname, lname, address, password, dob));
        Assertions.assertNull(user);
    }
}
