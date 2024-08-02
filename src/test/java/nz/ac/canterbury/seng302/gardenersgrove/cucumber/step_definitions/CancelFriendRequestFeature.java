package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.seng302.gardenersgrove.entity.FriendRequest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.FriendRequestRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendRequestService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserService userService;

    private ResultActions result;

    private static boolean usersCreated;

    private final User user1 = new User(
            "user1@mail.com",
            "Test", "User",
            "Password1!", "01/01/2000"
    );

    private final User user2 = new User(
            "user2@mail.com",
            "Test2", "User2",
            "Password1!", "01/01/2000"
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
        FriendRequest friendRequest = friendRequestService.findRequestBySender(user1).get(0);
        result = mockMvc.perform(MockMvcRequestBuilders.post(viewFriendsUri())
                .param("action","Cancel")
                .param("request",  friendRequest.getId().toString())
                .with(csrf()));
    }

    @Then("The other user cannot accept the request anymore")
    public void the_other_user_cannot_accept_the_request_anymore() throws Exception {
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
        if (!usersCreated) {
//            userService.addUsers(user1);
            userService.addUsers(user2);
            usersCreated = true;
        }
    }
}
