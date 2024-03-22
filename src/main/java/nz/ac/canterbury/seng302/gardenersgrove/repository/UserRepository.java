package nz.ac.canterbury.seng302.gardenersgrove.repository;

import java.util.List;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Users;
import org.h2.engine.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing User entities using Spring's CrudRepository.
 * Provides basic CRUD operations for User entities without the need to write custom implementations.
 */
@Repository
public interface UserRepository extends CrudRepository<Users, String> {

    /**
     * Retrieves all User entities.
     *
     * @return A list of all User entities.
     */
    List<Users> findAll();

    /**
     * Retrieves a specific user by email and password.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return A Users object if found, otherwise null.
     */
    Users findByEmailAndPassword(String email, String password);

    /**
     * Retrieves a specific user by email.
     *
     * @param email The email of the user.
     * @return A Users object if found, otherwise null.
     */
    Users findByEmail(String email);

    /**
     * Retrieves a specific user by ID.
     *
     * @param id The ID of the user.
     * @return A Users object if found, otherwise null.
     */
    Users findByUserId(int id);
}
