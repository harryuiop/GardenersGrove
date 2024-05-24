package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.FriendRequest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.friends.SearchedUserResult;
import nz.ac.canterbury.seng302.gardenersgrove.repository.FriendRequestRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendRequestService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
class UserServiceFriendTest {
    private UserRepository userRepositoryMock;
    private UserService userService;

    private FriendRequestService friendRequestService;

    private FriendRequestRepository friendRequestRepositoryMock;

    private User loggedInUser;
    private User friendUser;
    private User pendingRequestUser;
    private User declinedRequestUser;
    private List<User> mockRepositoryUsers;

    @BeforeEach
    void setUp() {
        userRepositoryMock = Mockito.mock(UserRepository.class);
        friendRequestRepositoryMock = Mockito.mock(FriendRequestRepository.class);
        userService = new UserService(userRepositoryMock);
        friendRequestService = new FriendRequestService(friendRequestRepositoryMock, userService);
        System.out.println("ok");
        loggedInUser = new User
                ("user1@gmail.com", "John", "Doe", "Password1!", "2000-01-01");
        pendingRequestUser = new User
                ("user2@gmail.com", "John", "Doe", "Password1!", "2000-01-01");
        friendUser = new User
                ("user3@gmail.com", "James", "Doe", "Password1!", "2000-01-01");
        declinedRequestUser = new User
                ("user4@gmail.com", "James", "Doe", "Password1!", "2000-01-01");

        mockRepositoryUsers = new ArrayList<>();
        mockRepositoryUsers.add(loggedInUser);
        mockRepositoryUsers.add(friendUser);
        mockRepositoryUsers.add(pendingRequestUser);
        mockRepositoryUsers.add(declinedRequestUser);
        when(userRepositoryMock.findAll()).thenReturn(mockRepositoryUsers);
    }

    @Test
    void searchUser_searchFriendByEmail_returnSearchedUser() {
        when(friendRequestRepositoryMock.findFriendRequestBySenderAndReceiver(any(), any()))
                .thenReturn(Arrays.asList(new FriendRequest(loggedInUser, pendingRequestUser)));


        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                "user2@gmail.com", loggedInUser, friendRequestService);
        Assertions.assertEquals(1, actualSearchedResults.size());
        Assertions.assertEquals(pendingRequestUser, actualSearchedResults.getFirst().getUser());
        Assertions.assertEquals(Status.PENDING.toString(), actualSearchedResults.getFirst().getStatusText());
    }
/*

    @Test
    void searchUser_searchByName_singleResult_returnSearchedUser() {

    }

    @Test
    void searchUser_searchByNameUser_multipleResults_returnSearchedUsers() {

    }

    @Test
    void searchUser_searchByPartialName_returnNoSearchedUsers() {

    }

    @Test
    void searchUser_searchByPartialEmail_returnNoSearchedUsers() {

    }
*/

}