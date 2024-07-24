package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.FormValuesValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.ProfanityCheckingException;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;
import static org.hamcrest.Matchers.hasToString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class PubliciseGardens {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private FormValuesValidator mockFormValuesValidator;

    private Garden garden;
    private Long gardenId;
    private Long latestGardenId;

    private String user1Id;
    private String user2Id;

    private String name;
    private String description;
    private String country;
    private String city;

    private URI formType;

    @Before
    public void setup() throws Exception {
        gardenRepository.deleteAll();
        userRepository.deleteAll();

        User user1 = new User("test@user.com", "Test", "User", "Password1!", "");
        User user2 = new User("other.test@user.com", "Test", "User", "Password1!", "");
        userRepository.save(user1);
        userRepository.save(user2);
        user1Id = "" + user1.getId();
        user2Id = "" + user2.getId();

        garden = new Garden (user1, "Garden 1", "Valid", new Location("NZ", "CHCH"), null, true);
        gardenRepository.save(garden);
        gardenId=garden.getId();
        latestGardenId = gardenId;

        name = "Garden";
        country = "NZ";
        city = "ChCh";
        description = "";
        Mockito.when(mockFormValuesValidator.checkProfanity(Mockito.anyString())).thenReturn(false);
    }

    @Given("I am on the garden details page for a garden I own")
    public void iAmOnTheGardenDetailsPageForAGardenIOwn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/garden/" + gardenId)
                        .with(user(user1Id))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @When("I mark a checkbox labelled “Make my garden public")
    public void iMarkACheckboxLabelledMakeMyGardenPublic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(makeGardenPublicUri(garden.getId()))
                .param("publicGarden", "true")
                .with(user(user1Id))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/garden/"+gardenId));
    }

    @Then("my garden will be visible in search results")
    public void myGardenWillBeVisibleInSearchResults() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(garden.getId()))
                .with(user(user2Id))
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Then("my garden is not visible in search results")
    public void myGardenIsNotVisibleInSearchResults() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(garden.getId()))
                    .with(user(user2Id)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Given("I am creating a new garden")
    public void iAmCreatingANewGarden() throws Exception {
        formType = newGardenUri();
        mockMvc.perform(MockMvcRequestBuilders.get(newGardenUri())
                    .with(user(user1Id))
                    .with(csrf()))
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
                .with(user(user1Id))
                .with(csrf())
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
        mockMvc.perform(MockMvcRequestBuilders.get(editGardenUri(gardenId))
                        .with(user(user1Id))
                        .with(csrf()))
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
                        .with(user(user1Id))
                        .with(csrf())
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
                        .with(user(user1Id))
                        .with(csrf())
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
                        .with(user(user1Id))
                        .with(csrf())
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
                        .param("publicGarden", "true")
                        .with(user(user1Id))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/garden/"+gardenId));
        Assertions.assertFalse(garden.isGardenPublic());
    }
}
