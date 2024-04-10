package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a user.
 * Represents user information including email, name, address, password, and roles.
 */
@Entity(name = "users")
public class User {

    /**
     * User email this will be a user ID and has to be unique and exist
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * User's First Name
     */
    @Column(name = "First_name", nullable = false)
    private String firstName;

    /**
     * User's Last Name This can be Null
     */
    @Column(name = "Last_name")
    private String lastName;

    /**
     * User Password for verification
     */
    @Column(nullable = false)
    private String password;

    /**
     * User's Date of Birth
     */
    @Column(name = "Date_of_Birth", nullable = false)
    private String dob;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_ID", nullable = false)
    private Integer userId;

    @Column(name = "profile_picture_file_name")
    private String profilePictureFileName;

    @Column
    private Boolean confirmation;

    @Column(name = "sign-up_token")
    private String token;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    /*
     * TODO - May be we need to create properties for GrantedAuthority
     * Perhaps this need to be a list?
     */
    // private Object roles;
    @Column
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private List<Authority> userRoles;

    /**
     * JPA Empty Constructor
     */
    protected User() {
    }

    /**
     * User Constructor to initialize user data.
     *
     * @param email    User email (unique identifier).
     * @param firstName    User's first name.
     * @param lastName    User's last name.
     * @param password User's password.
     * @param dob      User's date of birth.
     */
    public User(
            String email,
            String firstName,
            String lastName,
            String password,
            String dob
    ) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.dob = dob;
        this.confirmation = false;
        this.creationDate = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Retrieves the user's email.
     *
     * @return User's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the user's first name.
     *
     * @return User's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Retrieves the user's last name.
     *
     * @return User's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Retrieves the user's date of birth.
     *
     * @return User's date of birth.
     */
    public String getDob() {
        return dob;
    }

    /**
     * Retrieves the user's ID.
     *
     * @return User's ID.
     */
    public int getUserId() { return userId; }

    /**
     * Retrieves the user's password.
     *
     * @return User's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the file name of the user's profile picture.
     *
     * @return The file name of the user's profile picture.
     */
    public String getProfilePictureFileName() {
        return profilePictureFileName;
    }

    /**
     * Retrieves the file path of the user's profile.
     *
     * @return The path to the user's profile picture,
     * or to the default picture if they do not have one.
     */
    public String getProfilePictureFilePath() {
        if (profilePictureFileName != null) {
            return "/uploads/" + profilePictureFileName;
        } else {
            return "images/default_profile_photo.png";
        }
    }

    /**
     * getting user's valid token
     * @return user's valid token
     */
    public String getToken() {
        return this.token;
    }

    /**
     * Retrieve creation timestamp
     * @return Timestamp type of creation date
     */
    public Timestamp getCreationDateInMillisecond() {
        return creationDate;
    }

    /**
     * check if client's account is registered.
     * @return boolean about client's current status of registration
     */
    public boolean isConfirmed() {
        return this.confirmation;
    }

    /**
     * Sets the file name of the user's profile picture.
     *
     * @param profilePictureFileName The file name of the user's profile picture.
     */
    public void setProfilePictureFileName(String profilePictureFileName) {
        this.profilePictureFileName = profilePictureFileName;
    }

    /**
     * Setter method about client's registration token
     * @param token to authenticate the client's identity
     */
    public void setToken (String token) {
        this.token = token;
    }

    /**
     * setter method to change status of client's registration
     * @param confirmation
     */
    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    /**
     * Assigns roles to the user.
     * It takes a list of role names as input, creates Authority objects for each role, and adds them to the userRoles list.
     *
     * @param roles List of role names.
     */
    public void authoriseGranted(List<String> roles) {
        if (userRoles == null) userRoles = new ArrayList<>();
        for (String s : roles) {
            userRoles.add(new Authority(s));
        }
    }

    /**
     * Retrieves a list of authorities granted to the user.
     *
     * @return A list of GrantedAuthority objects.
     */
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        this.userRoles.forEach(authority ->
                authorities.add(new SimpleGrantedAuthority(authority.getRole()))
        );
        return authorities;
    }

    /**
     * Generates a string representation of the User object.
     *
     * @return A string representation of the User object.
     */
    public String toString() {
        return String.format(
                "User entity {first name: %s last name: %s " +
                        "email: %s password %s} is created",
                this.firstName,
                this.lastName,
                this.email,
                this.password
        );
    }
}
