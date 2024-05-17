package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.controller.GardenController;
import nz.ac.canterbury.seng302.gardenersgrove.entity.FriendRequest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.FriendRequestRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.PlantRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendRequestService;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendshipService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(GardenController.class);

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

    @Autowired
    private FriendshipService friendshipService;

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
            friendshipService.addFriend(user1, user);
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
                        .param("request", request.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/friends"));

        FriendRequest updatedRequest = friendRequestService.findRequestById(request.getId()).get();
        FriendRequest updatedRequest1 = friendRequestService.findRequestById(request1.getId()).get();
        List<User> userFriends = friendshipService.getFriends(user);
        assertEquals(1, userFriends.size());
        assertEquals(Status.DECLINED, updatedRequest.getStatus());
        assertEquals(1, friendRequestRepository.findFriendRequestsByReceiverAndStatus(user, Status.PENDING).size());
        assertEquals(Status.PENDING, updatedRequest1.getStatus());
    }

    @Test
    void accept_Request_Friend_Added() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(viewFriendsUri())
                        .param("action", "Accept")
                        .param("request", request.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/friends"));

        FriendRequest updatedRequest = friendRequestService.findRequestById(request.getId()).get();
        FriendRequest updatedRequest1 = friendRequestService.findRequestById(request1.getId()).get();
        List<User> userFriends = friendshipService.getFriends(user);
        assertEquals(2, userFriends.size());
        assertEquals(user2.getId(), userFriends.get(1).getId());
        assertEquals(Status.ACCEPTED, updatedRequest.getStatus());
        assertEquals(1, friendRequestRepository.findFriendRequestsByReceiverAndStatus(user, Status.PENDING).size());
        assertEquals(Status.PENDING, updatedRequest1.getStatus());
    }

}
