// Used for sending rest requests to deployed application
const possible_deployments = ['test', 'prod']

const tagField = document.getElementById('tagName');
const autocompleteList = document.getElementById('autocomplete-list');
const tagSubmitBtn = document.getElementById('tag-submit');
const debounceTimeMs = 100;

let timer;
let previousInput = "";

/**
 * Update autocomplete when users input for tag field is changed.
 * Set debouncing time is used.
 */
tagField.addEventListener('input', function() {
    clearTimeout(timer);
    timer = setTimeout(function() {
        const inputValue = tagField.value;
        if (inputValue) {
            if (inputValue !== previousInput) {
                previousInput = inputValue;
                updateAutocomplete(inputValue);
            }
        } else {
            removeAutocompleteBox();
        }
    }, debounceTimeMs);
});


/**
 * Call Java function to get the tag names.
 *
 * @param query Input from tag name search box.
 */
function updateAutocomplete(query) {
    const deployment = window.location.pathname.split('/')[1];
    const baseUri = deployment !== undefined && deployment in possible_deployments ?`/${deployment}` : '';
    fetch( `${baseUri}/tag/show-autocomplete?query=${query}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            renderAutocomplete(data);
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}

/**
 * Show the autocomplete box with the suggestions from tags in the database.
 *
 * @param tags list of tag suggestions.
 */
function renderAutocomplete(tags) {
    if (!tags || tags.length === 0) {
        console.log("No tags exist with given search.");
        return;
    }
    autocompleteList.innerHTML = '';
    showAutocompleteBox()
    tags.forEach(tag => {
        let suggestionElement = document.createElement("div");
        let primaryTextElement = document.createElement("div");
        primaryTextElement.classList.add("primary-text");

        primaryTextElement.innerHTML = tag;

        suggestionElement.addEventListener('click', function () {
            tagField.value = tag;
            tagSubmitBtn.click();
        });

        suggestionElement.appendChild(primaryTextElement);
        autocompleteList.appendChild(suggestionElement);
    });
    tagSubmitBtn.style.height = tagField.offsetHeight + 'px';
}

function removeAutocompleteBox() {
    autocompleteList.innerHTML = '';
    autocompleteList.style.display = 'none';
}

function showAutocompleteBox() {
    autocompleteList.style.display = 'block';
}



