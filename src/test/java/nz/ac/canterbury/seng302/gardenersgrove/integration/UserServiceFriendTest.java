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
    private User userSameName1;
    private User userSameName2;

    private List<User> mockRepositoryUsers;

    @BeforeEach
    void setUp() {
        userRepositoryMock = Mockito.mock(UserRepository.class);
        friendRequestRepositoryMock = Mockito.mock(FriendRequestRepository.class);
        userService = new UserService(userRepositoryMock);
        friendRequestService = new FriendRequestService(friendRequestRepositoryMock);

        loggedInUser = new User
                ("user1@gmail.com", "John", "Doe", "Password1!", "2000-01-01");
        pendingRequestUser = new User
                ("user2@gmail.com", "Jane", "Doe", "Password1!", "2000-01-01");
        friendUser = new User
                ("user3@gmail.com", "James", "Doe", "Password1!", "2000-01-01");
        declinedRequestUser = new User
                ("user4@gmail.com", "Jimmy", "Doe", "Password1!", "2000-01-01");

        userSameName1 = new User
                ("user5@gmail.com", "Jeffery", "Doe", "Password1!", "2000-01-01");

        userSameName2 = new User
                ("user6@gmail.com", "Jeffery", "Doe", "Password1!", "2000-01-01");

        mockRepositoryUsers = new ArrayList<>();
        mockRepositoryUsers.add(loggedInUser);
        mockRepositoryUsers.add(friendUser);
        mockRepositoryUsers.add(pendingRequestUser);
        mockRepositoryUsers.add(declinedRequestUser);
        mockRepositoryUsers.add(userSameName1);
        mockRepositoryUsers.add(userSameName2);
        when(userRepositoryMock.findAll()).thenReturn(mockRepositoryUsers);
    }

    @Test
    void searchUser_searchPendingFriendByEmail_returnSearchedUser() {
        when(friendRequestRepositoryMock.findFriendRequestBySenderAndReceiver(any(), any()))
                .thenReturn(Arrays.asList(new FriendRequest(loggedInUser, pendingRequestUser)));


        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                pendingRequestUser.getEmail(), loggedInUser, friendRequestService);

        Assertions.assertEquals(1, actualSearchedResults.size());
        Assertions.assertEquals(pendingRequestUser, actualSearchedResults.getFirst().getUser());
        Assertions.assertEquals(Status.PENDING.toString(), actualSearchedResults.getFirst().getStatusText());
    }

    @Test
    void searchUser_searchFriendByEmail_returnSearchedUser() {
        FriendRequest mockedRequest = new FriendRequest(loggedInUser, friendUser);
        mockedRequest.setStatus(Status.FRIENDS);
        when(friendRequestRepositoryMock.findFriendRequestBySenderAndReceiver(any(), any()))
                .thenReturn(Arrays.asList(mockedRequest));

        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                friendUser.getEmail(), loggedInUser, friendRequestService);

        Assertions.assertEquals(1, actualSearchedResults.size());
        Assertions.assertEquals(friendUser, actualSearchedResults.getFirst().getUser());
        Assertions.assertEquals(Status.FRIENDS.toString(), actualSearchedResults.getFirst().getStatusText());
    }

    @Test
    void searchUser_searchDeclinedRequestByEmail_returnSearchedUser() {
        FriendRequest mockedRequest = new FriendRequest(loggedInUser, declinedRequestUser);
        mockedRequest.setStatus(Status.DECLINED);
        when(friendRequestRepositoryMock.findFriendRequestBySenderAndReceiver(any(), any()))
                .thenReturn(Arrays.asList(mockedRequest));

        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                declinedRequestUser.getEmail(), loggedInUser, friendRequestService);

        Assertions.assertEquals(1, actualSearchedResults.size());
        Assertions.assertEquals(declinedRequestUser, actualSearchedResults.getFirst().getUser());
        Assertions.assertEquals(Status.DECLINED.toString(), actualSearchedResults.getFirst().getStatusText());
    }

    @Test
    void searchUser_searchByName_singleResult_returnSearchedUser() {
        when(friendRequestRepositoryMock.findFriendRequestBySenderAndReceiver(any(), any()))
                .thenReturn(Arrays.asList(new FriendRequest(loggedInUser, pendingRequestUser)));


        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                pendingRequestUser.getName(), loggedInUser, friendRequestService);

        Assertions.assertEquals(1, actualSearchedResults.size());
        Assertions.assertEquals(pendingRequestUser, actualSearchedResults.getFirst().getUser());
    }

    @Test
    void searchUser_searchByNameUser_multipleResults_returnSearchedUsers() {
        when(friendRequestRepositoryMock.findFriendRequestBySenderAndReceiver(any(), any()))
                .thenReturn(Arrays.asList(new FriendRequest(loggedInUser, userSameName1)));


        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                userSameName1.getName(), loggedInUser, friendRequestService);

        Assertions.assertEquals(2, actualSearchedResults.size());
        Assertions.assertEquals(userSameName1, actualSearchedResults.get(0).getUser());
        Assertions.assertEquals(userSameName2, actualSearchedResults.get(1).getUser());
    }

    @Test
    void searchUser_searchByPartialName_returnNoSearchedUsers() {
        when(friendRequestRepositoryMock.findFriendRequestBySenderAndReceiver(any(), any()))
                .thenReturn(Arrays.asList(new FriendRequest(loggedInUser, pendingRequestUser)));


        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                pendingRequestUser.getFirstName(), loggedInUser, friendRequestService);

        Assertions.assertEquals(0, actualSearchedResults.size());
    }

    @Test
    void searchUser_searchByPartialEmail_returnNoSearchedUsers() {
        when(friendRequestRepositoryMock.findFriendRequestBySenderAndReceiver(any(), any()))
                .thenReturn(Arrays.asList(new FriendRequest(loggedInUser, pendingRequestUser)));


        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                "@gmail.com", loggedInUser, friendRequestService);

        Assertions.assertEquals(0, actualSearchedResults.size());
    }

    @Test
    void searchUser_searchSelfByEmail_returnSelf() {
        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                loggedInUser.getEmail(), loggedInUser, friendRequestService);

        Assertions.assertEquals(1, actualSearchedResults.size());
        Assertions.assertEquals(loggedInUser, actualSearchedResults.getFirst().getUser());
        Assertions.assertEquals(Status.SELF.toString(), actualSearchedResults.getFirst().getStatusText());
    }

}