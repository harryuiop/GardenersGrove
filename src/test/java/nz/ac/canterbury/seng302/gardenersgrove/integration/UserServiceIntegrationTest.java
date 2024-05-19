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
                ("user1@gmail.com", "Ben", "Marshal", "Password1!", "2000-01-01");
        user2 = new User
                ("user2@gmail.com", "Marshal", "Smith,", "Password1!", "2000-01-01");
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

    @Test
    void getSearchedUser_searchJane_returnEmptyList() {
        List<User> mockRespositoryUsers = new ArrayList<>();
        mockRespositoryUsers.add(user1);
        mockRespositoryUsers.add(user2);
        when(userRepositoryMock.findAll()).thenReturn(mockRespositoryUsers);
        Assertions.assertEquals(new ArrayList<>(), userService.getSearchedUser("Jane"));
    }

    @Test
    void getSearchedUser_searchBen_returnUser1() {
        List<User> mockRespositoryUsers = new ArrayList<>();
        mockRespositoryUsers.add(user1);
        mockRespositoryUsers.add(user2);
        when(userRepositoryMock.findAll()).thenReturn(mockRespositoryUsers);
        List<Map<String,String>> users = new ArrayList<>();
        users.add(new HashMap<>());
        users.getFirst().put("email", user1.getEmail());
        users.getFirst().put("name", user1.getName());
        Assertions.assertEquals(users, userService.getSearchedUser("Ben"));
    }

    @Test
    void getSearchedUser_searchSmithWithRandomCasing_returnUser2() {
        List<User> mockRespositoryUsers = new ArrayList<>();
        mockRespositoryUsers.add(user1);
        mockRespositoryUsers.add(user2);
        when(userRepositoryMock.findAll()).thenReturn(mockRespositoryUsers);
        List<Map<String,String>> users = new ArrayList<>();
        users.add(new HashMap<>());
        users.getFirst().put("email", user2.getEmail());
        users.getFirst().put("name", user2.getName());
        Assertions.assertEquals(users, userService.getSearchedUser("Smith"));
    }

    @Test
    void getSearchedUser_searchMarshal_returnBothUsers() {
        List<User> mockRespositoryUsers = new ArrayList<>();
        mockRespositoryUsers.add(user1);
        mockRespositoryUsers.add(user2);
        when(userRepositoryMock.findAll()).thenReturn(mockRespositoryUsers);
        List<Map<String,String>> users = new ArrayList<>();
        users.add(new HashMap<>());
        users.getFirst().put("email", user1.getEmail());
        users.getFirst().put("name", user1.getName());
        users.add(new HashMap<>());
        users.getLast().put("email", user2.getEmail());
        users.getLast().put("name", user2.getName());
        Assertions.assertEquals(users, userService.getSearchedUser("Marshal"));
    }

    @Test
    void getSearchedUser_searchUser1AtGmail_returnUser1() {
        when(userRepositoryMock.findAll()).thenReturn(mockRepositoryUsers);
        List<User> users = new ArrayList<>();
        users.add(user1);
        Assertions.assertEquals(users, userService.getSearchedUser("user1@gmail.com"));
    }
    @Test
    void getSearchedUser_searchUser2AtGmail_returnUser1() {
        when(userRepositoryMock.findAll()).thenReturn(mockRepositoryUsers);
        List<User> users = new ArrayList<>();
        users.add(user2);
        Assertions.assertEquals(users, userService.getSearchedUser("user2@gmail.com"));
    }
    @Test
    void getSearchedUser_searchAtGmail_returnUser1And2() {
        when(userRepositoryMock.findAll()).thenReturn(mockRepositoryUsers);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        Assertions.assertEquals(users, userService.getSearchedUser("@gmail.com"));
    }
}
