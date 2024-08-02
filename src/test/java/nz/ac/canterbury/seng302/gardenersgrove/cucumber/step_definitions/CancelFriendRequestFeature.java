package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.seng302.gardenersgrove.cucumber.RunCucumberTest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.FriendRequest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendRequestService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.viewFriendsUri;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class CancelFriendRequestFeature {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private UserService userService;

    private ResultActions result;
    private Authentication auth;

    private User user1;

    private final User user2 = new User(
            "user2@mail.com",
            "Test", "User",
            "Password1!", ""
    );

    // AC 1
    @Given("I am on the manage friends page")
    public void i_am_on_the_manage_friends_page() {}

    @Given("I have have sent a friend request")
    public void i_have_have_sent_a_friend_request() {
        friendRequestService.sendFriendRequest(user1, user2);
    }

    @When("I cancel my friend request")
    public void i_cancel_my_friend_request() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(auth);
        FriendRequest friendRequest = friendRequestService.findRequestBySender(user1).get(0);
        result = mockMvc.perform(MockMvcRequestBuilders.post(viewFriendsUri())
                .param("action","Cancel")
                .param("request",  friendRequest.getId().toString())
                .with(csrf()));
    }

    @Then("The other user cannot accept the request anymore")
    public void the_other_user_cannot_accept_the_request_anymore() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(auth);
        FriendRequest friendRequest = friendRequestService.findRequestBySender(user1).get(0);
        result.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.forwardedUrl(viewFriendsUri().toString()));

        mockMvc.perform(MockMvcRequestBuilders.post(viewFriendsUri())
                .param("action", "Accept")
                .param("request", friendRequest.getId().toString())
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Given("I have two users")
    public void iHaveTwoUsers() {
        String email = "user1@mail.com";
        String password = "Password1!";
        if (userService.getUserByEmail(email) == null) {
            user1 = new User(email, "User", "One", password, "");
            user1.setConfirmation(true);
            user2.setConfirmation(true);
            userService.addUsers(user1);
            userService.addUsers(user2);
        }
        auth = RunCucumberTest.authMaker.accept(email, password, userService);
    }
}
