package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PubliciseGardens {
    @Given("I am on the garden details page for a garden I own")
    public void iAmOnTheGardenDetailsPageForAGardenIOwn() {
    }

    @When("I mark a checkbox labelled “Make my garden public")
    public void iMarkACheckboxLabelledMakeMyGardenPublic() {
    }

    @Then("my garden will be visible in search results")
    public void myGardenWillBeVisibleInSearchResults() {
    }

    @When("I do not mark a checkbox labelled Make my garden public")
    public void iDoNotMarkACheckboxLabelled() {
    }

    @Then("my garden is not visible in search results")
    public void myGardenIsNotVisibleInSearchResults() {
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
