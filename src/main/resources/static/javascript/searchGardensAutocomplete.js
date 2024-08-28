const searchBarField = document.getElementById('search-bar');
const autocompleteList = document.getElementById('autocomplete-list');

let previousInput = "";

/**
 * Listens for the user entering text into the search bar, and updates it if they enter new text.
 * If they clear the search bar, the suggestions box is removed.
 */
searchBarField.addEventListener('input', function() {
    const inputValue = searchBarField.value;
    if (inputValue) {
        if (inputValue !== previousInput) {
            previousInput = inputValue;
            updateAutocomplete(inputValue);
        }
    } else {
        removeAutocompleteBox();
    }
});

/**
 * When user clicks outside the street address input the autocomplete box disappears.
 */
searchBarField.addEventListener('blur', function() {
    removeAutocompleteBox();
});

function updateAutocomplete(inputValue) {
    //TODO: Make request for all the different tags and pass them into render
    renderAutocomplete(["test"]);
};

function renderAutocomplete(suggestions) {
    let suggestionList = suggestions;
    autocompleteList.innerHTML = '';
    showAutocompleteBox();
    suggestionList.forEach(item => {
        console.log(item);
        let suggestionElement = document.createElement("div");
        suggestionElement.classList.add("dropdown-item");
        suggestionElement.classList.add("border-bottom");
        suggestionElement.classList.add("text-wrap");
        suggestionElement.style.cursor = "pointer";
        let primaryTextElement = document.createElement("div");
        let secondaryTextElement = document.createElement("div",);
        primaryTextElement.classList.add("primary-text");
        secondaryTextElement.classList.add("secondary-text");

        let suggestionItem = item.streetAddress;

        primaryTextElement.innerHTML = suggestionItem;
        secondaryTextElement.innerHTML = "tag";

        suggestionElement.appendChild(primaryTextElement);
        suggestionElement.appendChild(secondaryTextElement);
        autocompleteList.appendChild(suggestionElement);
    });

    const dropdownItems = document.getElementsByClassName("dropdown-item");
    dropdownItems.item(dropdownItems.length - 1).classList.remove("border-bottom");
};


/**
 * Removes the content from the suggestions box and hides it.
 */
function removeAutocompleteBox() {
    autocompleteList.innerHTML = '';
    autocompleteList.style.display = 'none';
};

/**
 * Displays thew suggestions box.
 */
function showAutocompleteBox() {
    autocompleteList.style.display = 'block';
};
