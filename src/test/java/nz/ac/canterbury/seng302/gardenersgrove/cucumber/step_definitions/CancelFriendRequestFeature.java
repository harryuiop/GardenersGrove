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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.MANAGE_FRIENDS_URI_STRING;
import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.viewFriendsUri;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
public class CancelFriendRequestFeature {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private UserService userService;

    private ResultActions result;

    private Authentication auth;
    private FriendRequest friendRequest;

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
        SecurityContextHolder.getContext().setAuthentication(auth);
        friendRequest = friendRequestService.findRequestBySender(user1).get(0);
        result = mockMvc.perform(MockMvcRequestBuilders.post(viewFriendsUri())
                .param("action","Cancel")
                .param("request",  friendRequest.getId().toString())
                .with(csrf()));
    }

    @Then("The other user cannot accept the request anymore")
    public void the_other_user_cannot_accept_the_request_anymore() throws Exception {
        result.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(view().name("redirect:" +MANAGE_FRIENDS_URI_STRING));

        mockMvc.perform(MockMvcRequestBuilders.post(viewFriendsUri())
                        .param("action", "Accept")
                        .param("request", friendRequest.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Given("I have two users")
    public void iHaveTwoUsers() {
        if (userService.getUserByEmail(user1.getEmail()) == null) {
            userService.addUsers(user1);
        }
        if (userService.getUserByEmail(user2.getEmail()) == null) {
            userService.addUsers(user2);
        }
        auth = RunCucumberTest.authMaker.accept(user1.getEmail(), user1.getPassword(), userService);
    }
}
