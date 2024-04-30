package nz.ac.canterbury.seng302.gardenersgrove.repository;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing User entities using Spring's CrudRepository.
 * Provides basic CRUD operations for User entities without the need to write custom implementations.
 */
@Repository
public interface UserRepository extends CrudRepository<User, String> {



    /**
     * Retrieves all User entities.
     *
     * @return A list of all User entities.
     */
    List<User> findAll();

    /**
     * Retrieves a specific user by email and password.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return A User object if found, otherwise null.
     */
    User findByEmailAndPassword(String email, String password);

    /**
     * Retrieves a specific user by email.
     *
     * @param email The email of the user.
     * @return A User object if found, otherwise null.
     */
    User findByEmail(String email);

    /**
     * Retrieves a specific user by ID.
     *
     * @param id The ID of the user.
     * @return A User object if found, otherwise null.
     */
    User findByUserId(int id);

    /**
     * Retrieves a specific user By token
     *
     * @param token the 6 digits number to verify user.
     * @return A User object if found, otherwise return null.
     */
    User findByToken(String token);
}
