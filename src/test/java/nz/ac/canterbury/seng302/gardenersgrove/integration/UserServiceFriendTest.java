package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.FriendRequest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.friends.SearchedUserResult;
import nz.ac.canterbury.seng302.gardenersgrove.repository.FriendRequestRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.FriendshipRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendRequestService;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendshipService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
class UserServiceFriendTest {
    private UserRepository userRepositoryMock;
    private UserService userService;
    private FriendshipService friendshipService;
    private FriendshipRepository friendshipRepository;
    private FriendRequestService friendRequestService;

    private FriendRequestRepository friendRequestRepositoryMock;

    private User loggedInUser;
    private User friendUser;
    private User friendUserNoLastName;
    private User pendingRequestUser;
    private User declinedRequestUser;
    private User userSameName1;
    private User userSameName2;

    private List<User> mockRepositoryUsers;

    @BeforeEach
    void setUp() {
        userRepositoryMock = Mockito.mock(UserRepository.class);
        friendRequestRepositoryMock = Mockito.mock(FriendRequestRepository.class);
        friendshipRepository = Mockito.mock(FriendshipRepository.class);
        friendshipService = new FriendshipService(friendshipRepository);
        userService = new UserService(userRepositoryMock);
        friendRequestService = new FriendRequestService(friendRequestRepositoryMock);

        loggedInUser = new User
                ("user1@gmail.com", "John", "Doe", "Password1!", "2000-01-01");
        pendingRequestUser = new User
                ("user2@gmail.com", "Jane", "Doe", "Password1!", "2000-01-01");
        friendUser = new User
                ("user3@gmail.com", "James", "Doe", "Password1!", "2000-01-01");
        friendUserNoLastName = new User
                ("user5@gmail.com", "Frank", "", "Password1!", "2000-01-01");
        declinedRequestUser = new User
                ("user6@gmail.com", "Jimmy", "Doe", "Password1!", "2000-01-01");

        userSameName1 = new User
                ("user7@gmail.com", "friendUserNoLastNameJeffery", "Doe", "Password1!", "2000-01-01");

        userSameName2 = new User
                ("user8@gmail.com", "Jeffery", "Doe", "Password1!", "2000-01-01");

        mockRepositoryUsers = new ArrayList<>();
        mockRepositoryUsers.add(loggedInUser);
        mockRepositoryUsers.add(friendUser);
        mockRepositoryUsers.add(friendUserNoLastName);
        mockRepositoryUsers.add(pendingRequestUser);
        mockRepositoryUsers.add(declinedRequestUser);
        mockRepositoryUsers.add(userSameName1);
        mockRepositoryUsers.add(userSameName2);
        when(userRepositoryMock.findAll()).thenReturn(mockRepositoryUsers);
    }

    @Test
    void searchUser_searchPendingFriendByEmail_returnSearchedUser() {
        when(friendRequestRepositoryMock.findFriendRequestBySenderAndReceiver(any(), any()))
                .thenReturn(List.of(new FriendRequest(loggedInUser, pendingRequestUser)));


        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                pendingRequestUser.getEmail(), loggedInUser, friendRequestService, friendshipService);

        Assertions.assertEquals(1, actualSearchedResults.size());
        Assertions.assertEquals(pendingRequestUser, actualSearchedResults.getFirst().getUser());
        Assertions.assertEquals(Status.PENDING.toString(), actualSearchedResults.getFirst().getStatusText());
    }

    @Test
    void searchUser_searchFriendByEmail_returnSearchedUser() {
        FriendRequest mockedRequest = new FriendRequest(loggedInUser, friendUser);
        mockedRequest.setStatus(Status.FRIENDS);
        when(friendRequestRepositoryMock.findFriendRequestBySenderAndReceiver(any(), any()))
                .thenReturn(List.of(mockedRequest));

        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                friendUser.getEmail(), loggedInUser, friendRequestService, friendshipService);

        Assertions.assertEquals(1, actualSearchedResults.size());
        Assertions.assertEquals(friendUserNoLastName.getName(), actualSearchedResults.getFirst().getUser());
        Assertions.assertEquals(Status.FRIENDS.toString(), actualSearchedResults.getFirst().getStatusText());
    }

    @Test
    void searchUser_searchUserWithoutLastname_returnSearchedUser() {
        FriendRequest mockedRequest = new FriendRequest(loggedInUser, friendUserNoLastName);
        mockedRequest.setStatus(Status.FRIENDS);
        when(friendRequestRepositoryMock.findFriendRequestBySenderAndReceiver(any(), any()))
                .thenReturn(List.of(mockedRequest));

        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                friendUserNoLastName.getName(), loggedInUser, friendRequestService, friendshipService);

        Assertions.assertEquals(1, actualSearchedResults.size());
        Assertions.assertEquals(friendUserNoLastName, actualSearchedResults.getFirst().getUser());
        Assertions.assertEquals(Status.FRIENDS.toString(), actualSearchedResults.getFirst().getStatusText());
    }

    @Test
    void searchUser_searchDeclinedRequestByEmail_returnSearchedUser() {
        FriendRequest mockedRequest = new FriendRequest(loggedInUser, declinedRequestUser);
        mockedRequest.setStatus(Status.DECLINED);
        when(friendRequestRepositoryMock.findFriendRequestBySenderAndReceiver(any(), any()))
                .thenReturn(List.of(mockedRequest));

        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                declinedRequestUser.getEmail(), loggedInUser, friendRequestService, friendshipService);

        Assertions.assertEquals(1, actualSearchedResults.size());
        Assertions.assertEquals(declinedRequestUser, actualSearchedResults.getFirst().getUser());
        Assertions.assertEquals(Status.DECLINED.toString(), actualSearchedResults.getFirst().getStatusText());
    }

    @Test
    void searchUser_searchByName_singleResult_returnSearchedUser() {
        when(friendRequestRepositoryMock.findFriendRequestBySenderAndReceiver(any(), any()))
                .thenReturn(List.of(new FriendRequest(loggedInUser, pendingRequestUser)));


        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                pendingRequestUser.getName(), loggedInUser, friendRequestService, friendshipService);

        Assertions.assertEquals(1, actualSearchedResults.size());
        Assertions.assertEquals(pendingRequestUser, actualSearchedResults.getFirst().getUser());
    }

    @Test
    void searchUser_searchByNameUser_multipleResults_returnSearchedUsers() {
        when(friendRequestRepositoryMock.findFriendRequestBySenderAndReceiver(any(), any()))
                .thenReturn(List.of(new FriendRequest(loggedInUser, userSameName1)));


        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                userSameName1.getName(), loggedInUser, friendRequestService, friendshipService);

        Assertions.assertEquals(2, actualSearchedResults.size());
        Assertions.assertEquals(userSameName1, actualSearchedResults.get(0).getUser());
        Assertions.assertEquals(userSameName2, actualSearchedResults.get(1).getUser());
    }

    @Test
    void searchUser_searchByPartialName_returnNoSearchedUsers() {
        when(friendRequestRepositoryMock.findFriendRequestBySenderAndReceiver(any(), any()))
                .thenReturn(List.of(new FriendRequest(loggedInUser, pendingRequestUser)));


        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                pendingRequestUser.getFirstName(), loggedInUser, friendRequestService, friendshipService);

        Assertions.assertEquals(0, actualSearchedResults.size());
    }

    @Test
    void searchUser_searchByPartialEmail_returnNoSearchedUsers() {
        when(friendRequestRepositoryMock.findFriendRequestBySenderAndReceiver(any(), any()))
                .thenReturn(List.of(new FriendRequest(loggedInUser, pendingRequestUser)));


        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                "@gmail.com", loggedInUser, friendRequestService, friendshipService);

        Assertions.assertEquals(0, actualSearchedResults.size());
    }

    @Test
    void searchUser_searchSelfByEmail_returnSelf() {
        List<SearchedUserResult> actualSearchedResults = userService.getSearchedUserAndFriendStatus(
                loggedInUser.getEmail(), loggedInUser, friendRequestService, friendshipService);

        Assertions.assertEquals(1, actualSearchedResults.size());
        Assertions.assertEquals(loggedInUser, actualSearchedResults.getFirst().getUser());
        Assertions.assertEquals(Status.SELF.toString(), actualSearchedResults.getFirst().getStatusText());
    }

}