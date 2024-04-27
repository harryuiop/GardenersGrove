/**
 * When the checkbox is ticked to say there is no surname, clears and disables the last name field
 */
function updateCheckboxValue() {
    var checkbox = document.getElementById("noSurname");
    var lastName = document.getElementById("lastName")
    if (checkbox.checked) {
        checkbox.value = "true";
        lastName.value = "";
        lastName.disabled=true;
    } else {
        checkbox.value = "false";
        lastName.disabled=false;
    }
}

/**
 * When edit profile or register pages are re/loaded checks whether last name should be disabled or not.
 * Disables and checks the checkbox if the user has no last name.
 * Enables and unchecks the checkbox if the user has a last name.
 */
function loadingPage() {
    var checkbox = document.getElementById("noSurname");
    var lastName = document.getElementById("lastName")
    if (checkbox.value === "true") {
        checkbox.checked = true;
        lastName.value = "";
        lastName.disabled=true;
    } else {
        checkbox.value = "false";
        lastName.disabled=false;
    }
}