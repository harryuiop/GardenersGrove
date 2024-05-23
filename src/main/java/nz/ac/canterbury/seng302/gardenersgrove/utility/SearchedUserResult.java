package nz.ac.canterbury.seng302.gardenersgrove.utility;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;

public class SearchedUserResult {

    private User user;

    private String statusText;

    public SearchedUserResult(User user, String statusText) {
        this.user = user;
        this.statusText = statusText;
    }

    public User getUser() {
        return user;
    }

    public String getStatusText() {
        return statusText;
    }
}
