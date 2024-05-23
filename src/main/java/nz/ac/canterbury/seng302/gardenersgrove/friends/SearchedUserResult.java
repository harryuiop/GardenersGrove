package nz.ac.canterbury.seng302.gardenersgrove.friends;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.utility.Status;

/**
 * Result of search user, containing the actual user and their relationship to the authorized user.
 */
public class SearchedUserResult {
    private User user;
    private Status status;

    /**
     * Initializer
     *
     * @param user Found user entity
     * @param status Status of friend request, whether sent or not.
     */
    public SearchedUserResult(User user, Status status) {
        this.user = user;
        this.status = status;
    }

    /**
     * @return Logged in user
     */
    public User getUser() {
        return user;
    }

    /**
     * @return Status of friend request, whether sent or not.
     */
    public String getStatusText() {
        return status.toString();
    }
}
