package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

@Entity
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "email")
    private Users user;

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
