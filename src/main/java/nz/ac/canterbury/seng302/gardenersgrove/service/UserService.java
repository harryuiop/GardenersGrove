package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Integer.parseInt;
import static nz.ac.canterbury.seng302.gardenersgrove.controller.validation.UserValidation.*;

/**
 * Service class for interacting with User entity and UserRepository.
 * Provides methods to add users to persistence and retrieve users by email, password, or ID.
 */
@Service
public class UserService {

    /**
     * UserRepository instance to send user data to the database.
     */
    private final UserRepository userRepository;

    /**
     * Constructor for UserService.
     *
     * @param userRepository The UserRepository instance.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        User user1 = new User
                ("user1@gmail.com", "Default", "User", "Password1!", "2000-01-01");
        user1.setConfirmation(true);
        this.addUsers(user1);
        User user2 = new User
                ("user2@gmail.com", "John", "Doe", "Password1!", "2000-01-01");
        user2.setConfirmation(true);
        this.addUsers(user2);
    }

    /**
     * Adds a new user to persistence.
     *
     * @param newUser The new user data.
     * @return The UserRepository instance with the new user saved.
     */
    public User addUsers(User newUser) {
        if (!(emailIsValid(newUser.getEmail()) &&
                passwordIsValid(newUser.getPassword()) &&
                nameIsValid(newUser.getFirstName(), newUser.getLastName()) &&
                dobIsValid(newUser.getDob()) &&
                (getUserByEmail(newUser.getEmail()) == null))) {
            return null;
        }
        newUser.setPassword(hashUserPassword(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    /**
     * Persist changes to a user.
     *
     * @param user The user to update.
     */
    public boolean updateUser(User user) {
        if (
                emailIsValid(user.getEmail()) &&
                nameIsValid(user.getFirstName(), user.getLastName()) &&
                dobIsValid(user.getDob()) &&
                passwordIsValid(user.getPassword())
        ) {
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a user by email and password.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return The User object if found, otherwise null.
     */
    public User getUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    /**
     * Retrieves a user by email.
     *
     * @param email The email of the user.
     * @return The User object if found, otherwise null.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user.
     * @return The User object if found, otherwise null.
     */
    public User getUserById(int id) {
        return userRepository.findByUserId(id);
    }

    /**
     * Deletes a user by User object.
     *
     * @param user The user to delete
     */
    public void deleteUser(User user) { userRepository.delete(user); }

    /**
     * Retrieves the user object of the currently logged-in user
     * @return The User object if found
     */
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int currentPrincipalName = parseInt(auth.getName());
        return getUserById(currentPrincipalName);
    }

    /**

     * Retrieves a user by token
     *
     * @param token the 6 digits number to verify user.
     * @return A User object if found, otherwise return null.
     */
    public User getUserByToken(String token) {
        return userRepository.findByToken(token);
    }

    /**
     * grant a user a token and update user detail
     *
     * @param user a user data to be granted a token to verify
     * @return the same user from parameter that has token
     */
    public User grantUserToken(User user) {

        boolean isExist = false;
        String token = "";

        // execute until user got a unique token
        while(!isExist) {
            Random random = new Random();
            int digit = random.nextInt(10000, 1000000);
            token = String.format("%06d", digit);
            isExist = userRepository.findByToken(token) == null;
        }

        // update user
        user.setToken(token);
        this.updateUser(user);

        return user;
    }
    
    /**
     * Hashes a plain text password using BCrypt
     * @param passwordInPlainText The plain text password
     * @return  The hashed password
     */
    public String hashUserPassword(String passwordInPlainText) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);
        return encoder.encode(passwordInPlainText);
    }

    /**
     * Gets all the users in the repository and returns a list of them all.
     * @return a list of all the users in the repository.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Checks if any of the users' names or emails contain the given string.
     * @param searchString a string entered by the user in search of a user.
     * @return a list of users whos' names or emails contain the string.
     */
    public List<User> getSearchedUser(String searchString) {
        List<User> searchResults = new ArrayList<>();
        for (User user: getAllUsers()) {
            if ((user.getFirstName() + " " +user.getLastName()).toLowerCase().contains(searchString.toLowerCase()) ||
                user.getEmail().contains(searchString)) {
                searchResults.add(user);
            }
        }
        return searchResults;
    }
}
