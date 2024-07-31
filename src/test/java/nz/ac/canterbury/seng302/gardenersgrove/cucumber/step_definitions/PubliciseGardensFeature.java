package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.seng302.gardenersgrove.authentication.CustomAuthenticationProvider;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.FormValuesValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.ProfanityCheckingException;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;
import static org.hamcrest.Matchers.hasToString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class PubliciseGardensFeature {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GardenService gardenService;

    @Autowired
    private FormValuesValidator mockFormValuesValidator;

    private CustomAuthenticationProvider customAuthenticationProvider;

    private Garden garden;
    private Long gardenId;
    private Long latestGardenId;

    private String name;
    private String description;
    private String country;
    private String city;

    private URI formType;

    @Given("I have a garden")
    public void haveAGarden() {
        Location location = new Location("New Zealand", "Christchurch");
        garden = new Garden(userService.getUserByEmail("jane.doe@gmail.com"), "Garden", "",
                location, null, true);
        gardenService.saveGarden(garden);
        gardenId = garden.getId();
    }

    @Given("I have a user account that has logged in")
    public void iHaveAUserAccountThatHasLoggedIn() {
        User user = new User("jane.doe@gmail.com", "Jane", "Doe", "Password1!", "");
        customAuthenticationProvider = new CustomAuthenticationProvider(userService);
        user.setConfirmation(true);
        userService.addUsers(user);

        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(user.getEmail(), "Password1!");
        Authentication auth = customAuthenticationProvider.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

        Assertions.assertEquals(userService.getAuthenticatedUser().getUserId(), user.getUserId());
    }

    @Given("I am on the garden details page for a garden I own")
    public void iAmOnTheGardenDetailsPageForAGardenIOwn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(gardenId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("redirect:/garden/" + gardenId));
    }

    @When("I mark a checkbox labelled “Make my garden public")
    public void iMarkACheckboxLabelledMakeMyGardenPublic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(makeGardenPublicUri(garden.getId()))
                .param("publicGarden", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/garden/"+gardenId));
    }

    @Then("my garden will be visible in search results")
    public void myGardenWillBeVisibleInSearchResults() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(garden.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Then("my garden is not visible in search results")
    public void myGardenIsNotVisibleInSearchResults() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(garden.getId())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Given("I am creating a new garden")
    public void iAmCreatingANewGarden() throws Exception {
        formType = newGardenUri();
        mockMvc.perform(MockMvcRequestBuilders.get(newGardenUri()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("gardenForm"));
    }

    @When("I add an optional description of the garden")
    public void iAddAnOptionalDescriptionOfTheGarden() {
        description = "This is my newest garden";
    }

    @And("I submit the form")
    public void iSubmitTheForm() throws Exception {
        if (formType == null) {
            formType = editGardenUri(gardenId);
        }
        mockMvc.perform(MockMvcRequestBuilders.post(formType)
                .param("gardenName", name)
                .param("gardenSize", "")
                .param("gardenDescription", description)
                .param("country", country)
                .param("city", city)
                .param("streetAddress", "")
                .param("suburb", "")
                .param("postcode", ""))
            .andExpect(MockMvcResultMatchers.status().isOk());
        latestGardenId ++;
    }

    @And("the description is valid")
    public void theDescriptionIsValid() {
        Optional<Garden> gardenOptional = gardenRepository.findById(latestGardenId);
        gardenOptional.ifPresent(value -> Assertions.assertTrue(value.getVerifiedDescription()));
    }

    @When("I remove the description of the garden")
    public void iRemoveTheDescriptionOfTheGarden() {
        description = null;
    }

    @Then("the description is deleted")
    public void theDescriptionIsDeleted() {
        Optional<Garden> gardenOptional = gardenRepository.findById(latestGardenId);
        gardenOptional.ifPresent(value -> Assertions.assertNull(value.getDescription()));
    }

    @Given("I am editing an existing garden")
    public void iAmEditingAnExistingGarden() throws Exception {
        formType = editGardenUri(gardenId);
        mockMvc.perform(MockMvcRequestBuilders.get(editGardenUri(gardenId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("gardenForm"));
    }

    @Then("the description is persisted")
    public void theDescriptionIsPersisted() {
        Optional<Garden> gardenOptional;
        if (formType.equals(newGardenUri())) {
            gardenOptional = gardenRepository.findById(latestGardenId);
        } else {
            gardenOptional = gardenRepository.findById(gardenId);
        }
        gardenOptional.ifPresent(value -> Assertions.assertEquals(description, value.getDescription()));
    }

    @Given("I enter a description longer than 512 charaters")
    public void iEnterADescriptionLongerThan512Charaters() {
        description = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. " +
                "Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. " +
                "Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. " +
                "Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, " +
                "imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. " +
                "Cras dapibus. Vivamus el";

    }

    @Then("an error message tells me that “Description must be \"512\" characters or less and contain some text”")
    public void anErrorMessageTellsMeThatDescriptionMustBeCharactersOrLessAndContainSomeText() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                        .param("gardenName", name)
                        .param("gardenSize", "")
                        .param("gardenDescription", description)
                        .param("country", country)
                        .param("city", city)
                        .param("streetAddress", "")
                        .param("suburb", "")
                        .param("postcode", ""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(model().attribute("gardenDescriptionError",
                        hasToString("Description must be 512 characters or less and contain some text")));
    }

    @And("the description is not persisted.")
    public void theDescriptionIsNotPersisted() {
        Optional<Garden> gardenOptional = gardenRepository.findById(latestGardenId);
        gardenOptional.ifPresent(value -> Assertions.assertNotEquals(description, value.getDescription()));
    }

    @Given("I enter a description {string}")
    public void iEnterADescriptionString(String string) {
        description = string;
    }

    @Given("I enter a description that contains inappropriate words {string}")
    public void iEnterADescriptionThatContainsInappropriateWords(String string) throws ProfanityCheckingException, InterruptedException {
        description = string;
        Mockito.when(mockFormValuesValidator.checkProfanity(Mockito.anyString())).thenReturn(true);

    }

    @Then("an error message tells me that “The description does not match the language standards of the app.”")
    public void anErrorMessageTellsMeThatTheDescriptionDoesNotMatchTheLanguageStandardsOfTheApp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                        .param("gardenName", name)
                        .param("gardenSize", "")
                        .param("gardenDescription", description)
                        .param("country", country)
                        .param("city", city)
                        .param("streetAddress", "")
                        .param("suburb", "")
                        .param("postcode", ""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(model().attribute("gardenDescriptionError",
                        hasToString("The description does not match the language standards of the app")));
    }

    @Given("I enter some text into the description field")
    public void iEnterSomeTextIntoTheDescriptionField() {
        description = "A normal garden description";
    }

    @Then("I am informed my results was accepted but must be edited to be able to make public")
    public void iAmInformedMyResultsWasAcceptedButMustBeEditedToBeAbleToMakePublic() throws Exception {
        Mockito.when(mockFormValuesValidator.checkProfanity(description))
                .thenThrow(new ProfanityCheckingException("Failed to check for profanity"));
        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                        .param("gardenName", name)
                        .param("gardenSize", "")
                        .param("gardenDescription", description)
                        .param("country", country)
                        .param("city", city)
                        .param("streetAddress", "")
                        .param("suburb", "")
                        .param("postcode", ""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(model().attribute("profanityCheckWorked",
                        hasToString("false")));

        Optional<Garden> gardenOptional = gardenRepository.findById(latestGardenId);
        gardenOptional.ifPresent(value -> Assertions.assertFalse(value.getVerifiedDescription()));
    }

    @And("I cannot make the garden public")
    public void iCannotMakeTheGardenPublic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(makeGardenPublicUri(garden.getId()))
                        .param("publicGarden", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/garden/"+gardenId));
        Assertions.assertFalse(garden.isGardenPublic());
    }
}
