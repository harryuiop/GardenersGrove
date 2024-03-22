package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Users;
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
    @Autowired
    private UserRepository userRepository;

    /**
     * Constructor for UserService.
     *
     * @param userRepository The UserRepository instance.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Adds a new user to the database if there is no pre-existing user with
     * the same email address. Otherwise, the pre-exiting user's information
     * is updated
     *
     * @param newUser The new user data.
     * @return The UserRepository instance with the new user (or the current
     * user with new information) saved.
     */
    public Users addUsers(Users newUser) {
        Users currentUser = userRepository.findByEmail(newUser.getEmail());
        if (emailIsValid(newUser.getEmail()) &&
                passwordIsValid(newUser.getPassword()) &&
                nameIsValid(newUser.getFname(), newUser.getLname()) &&
                dobIsValid(newUser.getDob())) {
            if ((getUserByEmail(newUser.getEmail()) != null)) { // if there is a pre-existing user with the same email
                currentUser.fname = newUser.fname;
                currentUser.lname = newUser.lname;
                currentUser.password = newUser.password;
                currentUser.address = newUser.address;
                currentUser.dob = newUser.dob;
                return userRepository.save(currentUser);
            } else {
                return userRepository.save(newUser);
            }
        }
        return null;
    }

    /**
     * Retrieves a user by email and password.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return The Users object if found, otherwise null.
     */
    public Users getUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    /**
     * Retrieves a user by email.
     *
     * @param email The email of the user.
     * @return The Users object if found, otherwise null.
     */
    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user.
     * @return The Users object if found, otherwise null.
     */
    public Users getUserById(int id) {
        return userRepository.findByUserId(id);
    }
}
