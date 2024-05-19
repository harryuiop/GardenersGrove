package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

@Entity
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User friend1;

    @ManyToOne(optional = false)
    private User friend2;

    public Friendship(User friend1, User friend2) {
        this.friend1 = friend1;
        this.friend2 = friend2;
    }

    public Friendship() {

    }

    public User getFriend1() {
        return friend1;
    }

    public User getFriend2() {
        return friend2;
    }
}
