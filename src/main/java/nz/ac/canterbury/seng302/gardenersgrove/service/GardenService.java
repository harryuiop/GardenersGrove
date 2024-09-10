package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.controller.PublicGardensController;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.NoSuchFriendException;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GardenService {

    Logger logger = LoggerFactory.getLogger(PublicGardensController.class);
    private final GardenRepository gardenRepository;
    private final UserService userService;
    private final FriendshipService friendshipService;

    @Autowired
    public GardenService(GardenRepository gardenRepository, UserService userService, FriendshipService friendshipService) {
        this.gardenRepository = gardenRepository;
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    /**
     * Querys the database and grabs all gardens from the logged-in user
     *
     * @return List of all the authenticated users gardens
     */
    public List<Garden> getAllGardens() {
        return gardenRepository.findAllByOwner(userService.getAuthenticatedUser());
    }

    /**
     * Querys the database for all the given gardens of a friend
     *
     * @param friendId              ID of friend
     * @param userService           Logged in users user service
     * @return                      List of all the friends gardens
     * @throws NoSuchFriendException Thrown if no friend is found in the database
     */
    public List<Garden> getAllFriendsGardens(long friendId, UserService userService) throws NoSuchFriendException {
        if (friendshipService.getFriends(userService.getAuthenticatedUser()).contains(userService.getUserById(friendId))) {
            return gardenRepository.findAllByOwner(userService.getUserById(friendId));
        } else {
            throw new NoSuchFriendException(friendId);
        }
    }

    /**
     * Querys the database for a garden of a given ID
     *
     * @param id Garden ID
     * @return Returns a garden OR null
     */
    public Optional<Garden> getGardenById(Long id) {
        return gardenRepository.findById(id);
    }

    /**
     * Querys the database for a garden of a given Arduino ID
     *
     * @param id Arduino ID linked to the garden
     * @return Returns a garden OR null
     */
    public Garden getGardenByArduinoId(String id) {
        return gardenRepository.findByArduinoId(id);
    }

    /**
     * Saves a garden to the database
     *
     * @param garden Garden object to be saved
     * @return The given garden
     */
    public Garden saveGarden(Garden garden) {
        return gardenRepository.save(garden);
    }

    /**
     * Grabs the correct gardens to display on the page based on the page number through an SQL offset with optional search
     * parameter
     *
     * @param pageNumber Requested pagination page number
     * @param searchParameter Optional search parameter (plant within garden or garden itself)
     * @return The list of all public gardens that match the search string, or all public gardens if unspecified
     */
    public List<Garden> getPageOfPublicGardens(int pageNumber, String searchParameter, List<String> searchedTags) {
        boolean searchParameterGiven = searchParameter != null && !searchParameter.isEmpty();
        boolean searchedTagsGiven = !searchedTags.isEmpty();

        // Given there is a string search parameter
        if (searchParameterGiven) {
            // Given there is a search query AND tags
            if (searchedTagsGiven) {
                return gardenRepository.findByGardenPublicTrueWithSearchParameterAndTags(((pageNumber - 1) * 10), searchParameter, searchedTags);
            }
            // Given there is a search query AND no tags
            return gardenRepository.findByGardenPublicTrueWithSearchParameter(((pageNumber - 1) * 10), searchParameter);
        }
        // Given there is no search query AND tags
         if (searchedTagsGiven) {
            return gardenRepository.findByGardenPublicTrueWithTags(((pageNumber - 1) * 10), searchedTags);
        }
        return gardenRepository.findByGardenPublicTrue((pageNumber - 1) * 10);
    }

    /**
     * Query the entire database for total number of public gardens
     *
     * @return the number of total public gardens
     */
    public long countPublicGardens() {
        return gardenRepository.countByIsGardenPublicTrue();
    }

    /**
     * Query the database for total number of public gardens that match the search string
     *
     * @param searchParameter Search query
     * @return The number of total public gardens matching the search parameter
     */
    public long countPublicGardens(String searchParameter) {
        return gardenRepository.countByIsGardenPublicTrueWithGardenNameSearch(searchParameter);
    }

    /**
     * Query the database for total number of public gardens that match the search string
     *
     * @param searchParameter Search query
     * @param givenTags List of tags to query
     * @return The number of total public gardens matching the search parameter and given tags
     */
    public long countPublicGardens(String searchParameter, List<String> givenTags) {
        return gardenRepository.countFindByGardenPublicTrueWithSearchParameterAndTags(searchParameter, givenTags);
    }

    /**
     * Query the database for total number of public gardens that match the given tags
     *
     * @param givenTags List of tags to query
     * @return The number of total public gardens matching the given tags
     */
    public long countPublicGardens(List<String> givenTags) {
        return gardenRepository.countFindByGardenPublicTrueWithTags(givenTags);
    }

    /**
     * Deletes a garden
     * @param id The id of the garden to be deleted
     */
    public void deleteGarden(Long id) {
        gardenRepository.deleteById(id);
    }
}