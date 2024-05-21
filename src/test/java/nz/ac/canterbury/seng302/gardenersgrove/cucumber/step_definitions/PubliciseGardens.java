package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@CucumberContextConfiguration
@WithMockUser
public class PubliciseGardens {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GardenRepository gardenRepository;
    @Autowired
    private UserRepository userRepository;

    private Garden garden;
    private Long gardenId;
    private User user;

    private UserService userService;

    @Before
    public void setup() {
        gardenRepository.deleteAll();
        userRepository.deleteAll();
        userService = spy(new UserService(userRepository));
        user = new User("test@user.com", "Test", "User", "Password1!", "");
        userService.addUsers(user);
        garden = new Garden (user, "Garden 1", "Valid", new Location("NZ", "CHCH"), null, true);
        gardenRepository.save(garden);
        gardenId=garden.getId();
    }

    @Given("I am on the garden details page for a garden I own")
    public void iAmOnTheGardenDetailsPageForAGardenIOwn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(gardenId))).andExpect((MockMvcResultMatchers.status().isOk()));
    }

    @When("I mark a checkbox labelled “Make my garden public")
    public void iMarkACheckboxLabelledMakeMyGardenPublic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(makeGardenPublicUri(garden.getId()))
                .param("publicGarden", "true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("viewGarden"));
    }

    @Then("my garden will be visible in search results")
    public void myGardenWillBeVisibleInSearchResults() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(garden.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Then("my garden is not visible in search results")
    @WithMockUser(value="2")
    public void myGardenIsNotVisibleInSearchResults() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(garden.getId())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Given("I am creating a new garden")
    public void iAmCreatingANewGarden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(newGardenUri()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("gardenForm"));
    }

    @When("I add an optional description of the garden")
    public void iAddAnOptionalDescriptionOfTheGarden() {
    }

    @And("I submit the form")
    public void iSubmitTheForm() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post(newGardenUri())
//                .param("gardenName", gardenName)
//                .param("gardenSize", String.valueOf(size))
//                .param("description", description)
//                .param("country", country)
//                .param("city", city))
//            .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @And("the description is valid")
    public void theDescriptionIsValid() {
//        Assertions.assertTrue();
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
