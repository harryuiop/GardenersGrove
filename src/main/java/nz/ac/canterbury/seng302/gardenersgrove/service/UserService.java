package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Users;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static nz.ac.canterbury.seng302.gardenersgrove.validation.UserValidation.*;

/**
 * User Service to interact with User entity and User Repository
 */
@Service
public class UserService {

    /**
     * User Repository to send user data to database
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Constructor
     * 
     * @param userRepository user repository
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Add users to persistence
     * 
     * @param newUser new User data
     * @return a userRepository instance with the new user saved
     */
    public Users addUsers(Users newUser) {
        if (emailIsValid(newUser.getEmail()) &&
                passwordIsValid(newUser.getPassword()) &&
                    nameIsValid(newUser.getFname(), newUser.getLname()) &&
                            dobIsValid(newUser.getDob())) {
            return userRepository.save(newUser);
        }
        return null;
    }

    public Users getUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public Users getUserById(int id) {
        return userRepository.findByUserId(id);
    }

}
