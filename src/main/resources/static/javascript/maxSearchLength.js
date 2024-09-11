searchInput = document.getElementById("searchUsers")

/**
 * Check the length of th search and if it is less than 320 it submits the form otherwise displays an error
 */
function checkLength() {
    console.log(searchInput.value.length)
    let searchError;
    if (searchInput.value.length >= 320) {
        searchError = document.getElementById("searchError");
        searchError.style.visibility = "visible";
    } else {
        document.getElementById("searchUsersForm").submit()
    }
}

/**
 * When the enter button is pressed it check the input before submitting
 * From chatgpt
 * @param event used to stop the default event
 */
function preventEnterSubmit(event) {
    if (event.key === 'Enter') {
        event.preventDefault();
        checkLength()
    }
}

/**
 * Checks to see if the enter key has been pressed
 * Also From ChatGpt
 */
document.addEventListener('DOMContentLoaded', () => {
    const formElements = document.querySelectorAll('form input');
    formElements.forEach(element => {
        element.addEventListener('keydown', preventEnterSubmit);
    });
});