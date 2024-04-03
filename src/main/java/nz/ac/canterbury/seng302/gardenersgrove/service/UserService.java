package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private UserRepository userRepository;

    /**
     * Constructor for UserService.
     *
     * @param userRepository The UserRepository instance.
     */
    @Autowired

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Adds a new user to persistence.
     *
     * @param newUser The new user data.
     * @return The UserRepository instance with the new user saved.
     */
    public User addUsers(User newUser) {
        if (
                emailIsValid(newUser.getEmail()) &&
                        passwordIsValid(newUser.getPassword()) &&
                        nameIsValid(newUser.getFirstName(), newUser.getLastName()) &&
                        dobIsValid(newUser.getDob()) &&
                        (getUserByEmail(newUser.getEmail()) == null)
        ) {
            return userRepository.save(newUser);
        }
        return null;
    }

    /**
     * Persist changes to a user.
     *
     * @param user The user to update.
     */
    public boolean updateUser(User user) {
        if (
                emailIsValid(user.getEmail()) &&
                        passwordIsValid(user.getPassword()) &&
                        nameIsValid(user.getFirstName(), user.getLastName()) &&
                        dobIsValid(user.getDob())
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
}
