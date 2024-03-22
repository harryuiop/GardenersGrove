package nz.ac.canterbury.seng302.gardenersgrove.repository;

import java.util.List;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Users;
import org.h2.engine.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository to send User data to database
 */
@Repository
public interface UserRepository extends CrudRepository<Users, String> {
  /**
   * Find all Users
   *
   * @return a list of Users objects
   */
  List<Users> findAll();

  /**
   * Find a specific user by email and password
   *
   * @param email of the user
   * @param password created by the user
   * @return a Users object
   */
  Users findByEmailAndPassword(String email, String password);

  /**
   * Find a specific user by email
   *
   * @param email of the user
   * @return a Users object
   */
  Users findByEmail(String email);

  Users findByUserId(int id);
}
