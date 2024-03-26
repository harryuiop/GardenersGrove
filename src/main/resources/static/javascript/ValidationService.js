/**
 * JS file to do live input validation on user inputs for register and edit profile.
 * @type {RegExp}   Patterns to match for validation
 */

// Validation patterns
const namePattern = new RegExp("^[a-zA-Z\\-' ]+$");
const emailPattern = new RegExp(
  '^(([^<>()\\[\\]\\\\.,;:\\s@"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@"]+)*)|(".+"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$',
);
const passwordPattern = new RegExp(
  "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
);

// Live feedback colours
const validBorderColour = "#4CAF50";
const invalidBorderColour = "#FF0000";
const invalidBackgroundColour = "#FFF3F2";
const defaultBorderColour = "#CCC";
const defaultBackgroundColour = "white";

// Validation checks
let firstNameValid = false;
let lastNameValid = false;
let emailValid = false;
let passwordValid = false;
let passwordConfirmValid = false;
let ageValid = false;

/**
 * Sets the style to the unused state (gray and white).
 * @param formField     <input type=text>   The form being used.
 * @param errorMessage  <p>                 The error message.
 */
let setDefaultStyle = function (formField, errorMessage) {
  formField.style.borderColor = defaultBorderColour;
  formField.style.backgroundColor = defaultBackgroundColour;
  errorMessage.textContent = "";
};

/**
 * Sets the style to the invalid state (red).
 * @param formField     <input type=text>   The form being used.
 * @param errorMessage  <p>                 The error message.
 * @param errorText     string              The error text to be displayed.
 */
let setInvalidStyle = function (formField, errorMessage, errorText) {
  formField.style.borderColor = invalidBorderColour;
  formField.style.backgroundColor = invalidBackgroundColour;
  errorMessage.textContent = errorText;
};

/**
 * Sets the style to the valid state (green and white).
 * @param formField     <input type=text>   The form being used.
 * @param errorMessage  <p>                 The error message.
 */
let setValidStyle = function (formField, errorMessage) {
  formField.style.borderColor = validBorderColour;
  formField.style.backgroundColor = defaultBackgroundColour;
  errorMessage.textContent = "";
};

/**
 * Does live input validation of the first name field of a form.
 * @param form  <form> The input form.
 */
let firstNameValidation = function (form) {
  let firstNameInput = form.firstName;
  let errorMessage = document.getElementById("firstNameError");

  if (firstNameInput.value === "") {
    setDefaultStyle(firstNameInput, errorMessage);
    firstNameValid = false;
  } else if (!firstNameInput.value.match(namePattern)) {
    setInvalidStyle(
      firstNameInput,
      errorMessage,
      "Last name cannot be empty and must only include letters, spaces, hyphens or apostrophes",
    );
    firstNameValid = false;
  } else {
    setValidStyle(firstNameInput, errorMessage);
    firstNameValid = true;
  }
  refreshSubmitButton();
};

/**
 * Does live input validation of the last name field of a form.
 * @param form  <form> The input form.
 */
let lastNameValidation = function (form) {
  let lastNameInput = form.lastName;
  let errorMessage = document.getElementById("lastNameError");
  let noSurnameButton = document.getElementById("noSurname");
  if (noSurnameButton.checked === true) {
    // Sets the validation to valid if the no surname checkbox is ticked.
    setValidStyle(lastNameInput, errorMessage);
    lastNameInput.value = "";
    lastNameValid = true;
  } else if (lastNameInput.value === "") {
    setDefaultStyle(lastNameInput, errorMessage);
    lastNameValid = false;
  } else if (!lastNameInput.value.match(namePattern)) {
    setInvalidStyle(
      lastNameInput,
      errorMessage,
      "First/Last name cannot be empty and must only include letters, spaces, hyphens or apostrophes",
    );
    lastNameValid = false;
  } else {
    setValidStyle(lastNameInput, errorMessage);
    lastNameValid = true;
  }
  refreshSubmitButton();
};

/**
 * Check email validation when user try to register
 * or try to update their profile
 * @param form  <form> The input form.
 */
let checkEmailValidation = function (form) {
  emailValidation(form);
  //if emailValidaton pass then we check user's new email whether it is already in database
  if (emailValid === true) {
    checkEmail(form);
  }
  refreshSubmitButton();
};

/**
 * check user email and return error message
 * if user input email is already exist in database
 * @param form  <form> The input form.
 */
let checkEmail = function (form) {
  const email = form.email;
  const errorMessage = document.getElementById("emailError");

  //Open XMLHttpRequest
  let xhr = new XMLHttpRequest();
  // Configure request
  xhr.open("POST", "/check-email-duplication", true);
  xhr.setRequestHeader("Content-Type", "text/plain");

  xhr.onreadystatechange = function () {
    if (xhr.readyState === XMLHttpRequest.DONE) {
      // handle response here when request is successful
      if (xhr.status === 200) {
        const isValid = xhr.responseText;
        console.log("Received data: ", isValid);
        // Handles isValid here
        if (isValid === "true") {
          setInvalidStyle(email, errorMessage, "Email is already in use.");
          emailValid = false;
        } else {
          setValidStyle(email, errorMessage);
          emailValid = true;
        }
      } else {
        // error handling here when request is failed
        console.error("ERROR Status:", xhr.status);
      }
    }
  };
  // send data
  xhr.send(email.value);
};

/**
 * Does live input validation of the email field of a form.
 * @param form  <form> The input form.
 */
let emailValidation = function (form) {
  let emailInput = form.email;
  let errorMessage = document.getElementById("emailError");

  if (emailInput.value === "") {
    setDefaultStyle(emailInput, errorMessage);
    emailValid = false;
  } else if (!emailInput.value.match(emailPattern)) {
    setInvalidStyle(
      emailInput,
      errorMessage,
      "Email address must be in the form ‘jane@doe.nz’",
    );
    emailValid = false;
  } else {
    setValidStyle(emailInput, errorMessage);
    emailValid = true;
  }
  refreshSubmitButton();
};

/**
 * Does live input validation of the password field of a form.
 * @param form  <form> The input form.
 */
passwordValidation = function (form) {
  let passwordInput = form.password;
  let errorMessage = document.getElementById("passwordError");

  if (passwordInput.value === "") {
    setDefaultStyle(passwordInput, errorMessage);
    passwordValid = false;
  } else if (!passwordInput.value.match(passwordPattern)) {
    setInvalidStyle(
      passwordInput,
      errorMessage,
      "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number,  and one special character.",
    );
    passwordValid = false;
  } else {
    setValidStyle(passwordInput, errorMessage);
    passwordValid = true;
  }
  refreshSubmitButton();
};

/**
 * Does live input validation of the confirmPassword field of a form.
 * @param form  <form> The input form.
 */
confirmPasswordValidation = function (form) {
  let passwordConfirmInput = form.passwordConfirm;
  let passwordInput = form.password;
  let errorMessage = document.getElementById("passwordConfirmError");

  if (passwordConfirmInput.value === "") {
    setDefaultStyle(passwordConfirmInput, errorMessage);
    passwordConfirmValid = false;
  } else if (passwordConfirmInput.value !== passwordInput.value) {
    setInvalidStyle(
      passwordConfirmInput,
      errorMessage,
      "Passwords do not match",
    );
    passwordConfirmValid = false;
  } else {
    setValidStyle(passwordConfirmInput, errorMessage);
    passwordConfirmValid = true;
  }
  refreshSubmitButton();
};

/**
 * Does live input validation of the date of birth (age) field of a form.
 * @param form  <form> The input form.
 */
ageValidation = function (form) {
  let dateOfBirthInput = form.dateOfBirth;
  let errorMessage = document.getElementById("dateOfBirthError");

  let dobDate = new Date(dateOfBirthInput.value);
  let today = new Date();
  let thirteenInMs = 410240376000;
  let oneTwentyInMs = 3786834240000;
  let ageLessThenThirteen = today - dobDate < thirteenInMs;
  let ageOverOneTwenty = today - dobDate > oneTwentyInMs;

  console.log(dateOfBirthInput.value);

  if (dateOfBirthInput.value === "") {
    setDefaultStyle(dateOfBirthInput, errorMessage);
    ageValid = false;
  } else if (ageLessThenThirteen) {
    setInvalidStyle(
      dateOfBirthInput,
      errorMessage,
      "You must be 13 years or older to create an account",
    );
    ageValid = false;
  } else if (ageOverOneTwenty) {
    setInvalidStyle(
      dateOfBirthInput,
      errorMessage,
      "The maximum age allowed is 120 years",
    );
    ageValid = false;
  } else {
    setValidStyle(dateOfBirthInput, errorMessage);
    ageValid = true;
  }
  refreshSubmitButton();
};

/**
 * Sets the last name input value to "" and sets the style to valid if the noSurname button is checked.
 * @param form <form> The input form.
 */
checkNoSurnameClicked = function (form) {
  let noSurnameButton = document.getElementById("noSurname");
  let lastNameInput = form.lastName;

  if (noSurnameButton.checked === true) {
    lastNameInput.value = "";
    lastNameValid = true;
    setValidStyle(lastNameInput);
  }
};

/**
 * Refreshes the submit button to check if all the fields are valid, enables it if so,
 * otherwise sets it to be disabled.
 */
refreshSubmitButton = function () {
  let submitButton = document.getElementById("submit");
  if (
    firstNameValid &&
    lastNameValid &&
    emailValid &&
    passwordValid &&
    passwordConfirmValid &&
    ageValid
  ) {
    submitButton.style.backgroundColor = validBorderColour;
    submitButton.disabled = false;
    submitButton.style.color = "white";
  } else {
    submitButton.style.backgroundColor = defaultBorderColour;
    submitButton.disabled = true;
  }
};
