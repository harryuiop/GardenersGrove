package nz.ac.canterbury.seng302.gardenersgrove.utility;

/**
 * Status of a friend request whether the request exists or not.
 */
public enum Status {

    ACCEPTED("Accepted"),
    PENDING("Pending"),
    DECLINED("Declined"),
    FRIENDS("Friends"),
    SEND_REQUEST("Invite as friend"),
    SELF("Self");

    public final String string;

    private Status(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
