package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

/**
 * Entity class representing a password reset.
 * Includes the token, UUID and related user id.
 */
@Entity
public class ResetPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String token;

    @Column
    private long userId;

    /**
     * JPA Empty Constructor
     */
    protected ResetPasswordToken() {
    }

    /**
     * User Constructor to initialize reset password data.
     *
     * @param token A UUID reset token
     * @param userId Related user by id.
     */
    public ResetPasswordToken(String token, long userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }
}
