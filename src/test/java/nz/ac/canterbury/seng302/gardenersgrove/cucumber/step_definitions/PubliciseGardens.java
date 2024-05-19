package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import nz.ac.canterbury.seng302.gardenersgrove.GardenersGroveApplication;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.viewGardenUri;
import static org.mockito.Mockito.spy;

@SpringBootTest
@WebAppConfiguration
@CucumberContextConfiguration
@ContextConfiguration(classes = GardenersGroveApplication.class)
public class PubliciseGardens {
    @Autowired
    private static MockMvc mockMvc;
    private static UserService userService;
    private static GardenService gardenService;
    private static User user1;
    private static User user2;
    private static Garden garden;
    private static Location location;

    @BeforeAll
    public static void before_or_after_all() {
        mockMvc = MockMvcBuilders.standaloneSetup(new GardenersGroveApplication()).build();
        userService = Mockito.mock(UserService.class);
        gardenService = Mockito.mock(GardenService.class);

        user1 = new User("user@gmail.com", "Test", "User", "Password1!", null);
        user2 = new User("other.user@gmail.com", "Other", "User", "Password1!", null);

        location = new Location("New Zealand", "Christchurch");
        garden = spy(new Garden(user1, "Garden", "", location, 1F, true));
        Mockito.when(garden.getId()).thenReturn(1L);
        Mockito.when(gardenService.getGardenById(Mockito.anyLong())).thenReturn(Optional.of(garden));
    }

    @Given("I am on the garden details page for a garden I own")
    public void iAmOnTheGardenDetailsPageForAGardenIOwn() throws Exception {
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(user1);
        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(garden.getId()))).andExpect((MockMvcResultMatchers.status().isOk()));
    }

    @When("I mark a checkbox labelled “Make my garden public")
    public void iMarkACheckboxLabelledMakeMyGardenPublic() {
        garden.setPublicGarden(true);
    }

    @Then("my garden will be visible in search results")
    public void myGardenWillBeVisibleInSearchResults() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(garden.getId()))).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Then("my garden is not visible in search results")
    public void myGardenIsNotVisibleInSearchResults() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(garden.getId()))).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Given("I am creating a new garden")
    public void iAmCreatingANewGarden() {
    }

    @When("I add an optional description of the garden")
    public void iAddAnOptionalDescriptionOfTheGarden() {
    }

    @And("I submit the form")
    public void iSubmitTheForm() {
    }

    @And("the description is valid")
    public void theDescriptionIsValid() {
    }

    @When("I remove the description of the garden")
    public void iRemoveTheDescriptionOfTheGarden() {
    }

    @Then("the description is deleted")
    public void theDescriptionIsDeleted() {
    }

    @Given("I am editing an existing garden")
    public void iAmEditingAnExistingGarden() {
    }

    @And("I submit the form, and the description is valid")
    public void iSubmitTheFormAndTheDescriptionIsValid() {
    }

    @Then("the description is persisted")
    public void theDescriptionIsPersisted() {
    }

    @Given("I enter an invalid description \\(i.e. more than 512 characters long, or contains only special characters and numbers )")
    public void iEnterAnInvalidDescriptionIEMoreThanCharactersLongOrContainsOnlySpecialCharactersAndNumbers() {
    }

    @Then("an error message tells me that “Description must be \"512\" characters or less and contain some text”")
    public void anErrorMessageTellsMeThatDescriptionMustBeCharactersOrLessAndContainSomeText() {
    }

    @And("the description is not persisted.")
    public void theDescriptionIsNotPersisted() {
    }

    @Given("I enter a description that contains inappropriate words")
    public void iEnterADescriptionThatContainsInappropriateWords() {
    }

    @Then("an error message tells me that “The description does not match the language standards of the app.”")
    public void anErrorMessageTellsMeThatTheDescriptionDoesNotMatchTheLanguageStandardsOfTheApp() {
    }

    @Given("I enter some text into the description field")
    public void iEnterSomeTextIntoTheDescriptionField() {
    }

    @Then("I see an indication of the length of the input text such as “x\\/512” characters \\(where x is the current number of characters in the input)")
    public void iSeeAnIndicationOfTheLengthOfTheInputTextSuchAsXCharactersWhereXIsTheCurrentNumberOfCharactersInTheInput() {
    }

    @Given("I enter a description")
    public void iEnterADescription() {
    }

    @Then("I am informed my results was accepted but must be editited to be able to make public")
    public void iAmInformedMyResultsWasAcceptedButMustBeEdititedToBeAbleToMakePublic() {
    }

    @And("the decription is persisted")
    public void theDecriptionIsPersisted() {
    }

    @And("I cannot make the garden public")
    public void iCannotMakeTheGardenPublic() {

    }

}
