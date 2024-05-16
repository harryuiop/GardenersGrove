package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.FriendRequest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.FriendRequestRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.PlantRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendRequestService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.viewFriendsUri;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
public class FriendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendRequestService friendRequestService;

    @SpyBean
    private UserService userService;

    private User user;

    private User user1;

    private User user2;

    private User user3;

    private FriendRequest request;

    private FriendRequest request1;

    /**
     * Set up user and user1 as friends. Friend requests sent to user from user2 and user3.
     */
    @BeforeEach
    void setUp() {
        plantRepository.deleteAll();
        gardenRepository.deleteAll();
        friendRequestRepository.deleteAll();
        userRepository.deleteAll();

        if (user == null) {
            user = new User("test@domain.net", "Test", "User", "Password1!", "2000-01-01");
            user.setConfirmation(true);
            userRepository.save(user);
        }
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(user);

        if (user1 == null) {
            user1 = new User("test1@domain.net", "Test", "User", "Password1!", "2000-01-01");
            user1.setConfirmation(true);
            userRepository.save(user1);
            user.addFriend(user1);
            user1.addFriend(user);
            userRepository.save(user);
            userRepository.save(user1);
        }

        if (user2 == null) {
            user2 = new User("test2@domain.net", "Test", "User", "Password1!", "2000-01-01");
            user2.setConfirmation(true);
            userRepository.save(user2);
        }

        if (user3 == null) {
            user3 = new User("test3@domain.net", "Test", "User", "Password1!", "2000-01-01");
            user3.setConfirmation(true);
            userRepository.save(user3);
        }

        if (request == null) {
            request = new FriendRequest(user2, user);
            friendRequestRepository.save(request);
        }

        if (request1 == null) {
            request1 = new FriendRequest(user3, user);
            friendRequestRepository.save(request1);
        }
    }

    @Test
    void decline_Request_Request_Removed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(viewFriendsUri())
                        .param("action", "Decline")
                        .param("request", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("manageFriends"));

        List<User> userFriends = user.getFriends();
        assertEquals(1, userFriends.size());
        assertEquals(Status.DECLINED, request.getStatus());
        assertEquals(1, friendRequestRepository.findFriendRequestsByReceiverAndStatus(user, Status.PENDING).size());
        assertEquals(Status.PENDING, request1.getStatus());
    }

    @Test
    void accept_Request_Friend_Added() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(viewFriendsUri())
                        .param("action", "Accept")
                        .param("request", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("manageFriends"));

        List<User> userFriends = user.getFriends();
        assertEquals(2, userFriends.size());
        assertEquals(user2, userFriends.get(1));
        assertEquals(Status.ACCEPTED, request.getStatus());
        assertEquals(1, friendRequestRepository.findFriendRequestsByReceiverAndStatus(user, Status.PENDING).size());
        assertEquals(Status.PENDING, request1.getStatus());
    }

}
