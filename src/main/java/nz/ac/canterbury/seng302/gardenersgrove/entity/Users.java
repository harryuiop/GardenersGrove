package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Primary;

/**
 * Entity for User. This class will add a user data from register page to send
 * it to UserRepository
 */
@Entity
public class Users {

  /**
   * User email this will be a user ID and has to be unique and exist
   */
  @Column(name = "email", nullable = false)
  private String email;

  /**
   * Users First Name
   */
  @Column(name = "First_name", nullable = false)
  private String fname;

  /**
   * Users Last Name This can be Null
   */
  @Column(name = "Last_name")
  private String lname;

  /**
   * User's Home address
   */
  @Column(nullable = false)
  private String address;

  /**
   * User Password for verification
   */
  @Column(nullable = false)
  private String password;

  /**
   * Users Date of Birth
   */
  @Column(name = "Date_of_Birth", nullable = false)
  private String dob;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "User_ID", nullable = false)
  private Integer userId;



  /**
   * JPA Empty Constructor
   */
  protected Users() {}

  /**
   * User Constructor to add user data to this class
   *
   * @param email    User email this will be a user ID and has to be unique and
   *                 exist
   * @param fname    User First Name
   * @param lname    Users Last Name This can be null
   * @param address  User Home address
   * @param password User Password to verify
   * @param dob      User's date of Birth
   */
  public Users(
    String email,
    String fname,
    String lname,
    String address,
    String password,
    String dob
  ) {
    this.email = email;
    this.fname = fname;
    this.lname = lname;
    this.address = address;
    this.password = password;
    this.dob = dob;
//    this.imagePath = "css/images/default_profile_photo.png";
  }

  /**
   * Get User Email
   *
   * @return User Email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Get User First Name
   *
   * @return User First Name
   */
  public String getFname() {
    return fname;
  }

  /**
   * User Last Name
   *
   * @return
   */
  public String getLname() {
    return lname;
  }

  /**
   * Get user home address
   *
   * @return User Home address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Get user date of birth
   *
   * @return user date of birth
   */
  public String getDob() {
    return dob;
  }

  public int getUserId() { return userId; }


  /**
   * Get the user password
   *
   * @return user password
   */
  public String getPassword() {
    return password;
  }


  /**
   * To String method for this class
   *
   * @return brief information about this Class
   */
  public String toString() {
    return String.format(
      "User entity {first name: %s last name: %s " +
      "email: %s password %s} is created",
      this.fname,
      this.lname,
      this.email,
      this.password
    );
  }
}
