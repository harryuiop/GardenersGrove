package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;
/**
 * Entity class representing an Authority.
 * Represents an Authority in which defines the authority level of a specific user defining which endpoints are accessable
 * to a user depeneding on their role.
 *
 * The definition of which roles can access what endpoints is defined in authentication/SecurityConfiguration in the
 * function SecurityFilterChain
 */
@Entity
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "email")
    private User user;

    @Column
    private String role;

    /**
     * Default constructor for JPA.
     */
    protected Authority() {
        // JPA empty constructor
    }

    /**
     * Constructor for creating an Authority with a role.
     *
     * @param role The role of the authority.
     */
    public Authority(String role) {
        this.role = role;
    }

    /**
     * Retrieves the role associated with this authority.
     *
     * @return The role of the authority.
     */
    public String getRole() {
        return role;
    }
}
