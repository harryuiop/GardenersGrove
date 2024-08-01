package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class EditUserProfileFeature {
    @When("I visit the edit profile page")
    public void iVisitTheEditProfilePage() {
    }

    @Then("My details are populated into the fields except my password")
    public void myDetailsArePopulatedIntoTheFieldsExceptMyPassword() {
    }

    @And("My user account does not have a last name")
    public void myUserAccountDoesNotHaveALastName() {
    }

    @Then("The \"I have no surname\" checkbox is marked as checked")
    public void theCheckboxIsMarkedAsChecked() {
    }

    @When("I enter valid values for first name, last name, email, address and date of birth on the edit profile page")
    public void iEnterValidValuesForFirstNameLastNameEmailAddressAndDateOfBirthOnTheEditProfilePage() {
    }

    @And("I click the submit button")
    public void iClickTheSubmitButton() {
    }

    @Then("my new details are saved")
    public void myNewDetailsAreSaved() {
    }

    @And("I am taken to the profile page")
    public void iAmTakenToTheProfilePage() {
    }

    @And("My user account has a saved last name")
    public void myUserAccountHasASavedLastName() {
    }

    @When("I check the \"I have no surname\" check box on the edit profile page")
    public void iCheckTheCheckBoxOnTheEditProfilePage() {
    }

    @Then("my old surname has been removed from my account")
    public void myOldSurnameHasBeenRemovedFromMyAccount() {
    }

    @When("I enter a blank value for first name on the edit profile page")
    public void iEnterABlankValueForFirstNameOnTheEditProfilePage() {
    }

    @Then("an error message tells me {string}")
    public void anErrorMessageTellsMe(String errorMessage) {
    }

    @When("I enter a blank value for last name on the edit profile page")
    public void iEnterABlankValueForLastNameOnTheEditProfilePage() {
    }

    @When("I enter the invalid {string} in the first name field on the edit profile page")
    public void iEnterTheInvalidStringInTheFirstNameFieldOnTheEditProfilePage(String name) {
    }

    @Then("I get an error message explaining the name validation rules")
    public void iGetAnErrorMessageExplainingTheNameValidationRules() {
    }

    @When("I enter the invalid {string} in the last name field on the edit profile page")
    public void iEnterTheInvalidStringInTheLastNameFieldOnTheEditProfilePage(String name) {
    }

    @When("I enter a name longer than {int} characters into the first name field")
    public void iEnterANameLongerThanCharactersIntoTheFirstNameField(int maxLength) {
    }

    @Then("I get an error message saying {string}")
    public void iGetAnErrorMessageSaying(String errorMessage) {
    }

    @When("I enter a name longer than {int} characters into the last name field")
    public void iEnterANameLongerThanCharactersIntoTheLastNameField(int maxLength) {
    }

    @When("I enter a malformed {email} into the email field on the edit profile page")
    public void iEnterAMalformedEmailIntoTheEmailFieldOnTheEditProfilePage(String email) {
    }

    @Then("I get an error message saying \"Email address must be in the form ‘jane@doe.nz’\"")
    public void iGetAnErrorMessageSayingEmailAddressMustBeInTheFormJaneDoeNz() {
    }

    @When("I enter an already used email address into the email field on the edit profile page")
    public void iEnterAnAlreadyUsedEmailAddressIntoTheEmailFieldOnTheEditProfilePage() {
    }

    @When("I enter a date of birth younger then {int} years old in the date of birth field on the edit profile page")
    public void iEnterADateOfBirthYoungerThenYearsOldInTheDateOfBirthFieldOnTheEditProfilePage(int minAge) {
    }

    @When("I enter a date of birth older than {int} years old in the date of birth field on the edit profile page")
    public void iEnterADateOfBirthOlderThanYearsOldInTheDateOfBirthFieldOnTheEditProfilePage(int maxAge) {
    }

    @And("I enter any changes in the fields")
    public void iEnterAnyChangesInTheFields() {
    }

    @And("I click the \"Cancel\" button")
    public void iClickTheButton(String arg0) {
    }

    @Then("The changes are not saved")
    public void theChangesAreNotSaved() {
    }
}
