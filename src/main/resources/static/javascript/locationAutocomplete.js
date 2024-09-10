// Used for sending rest requests to deployed application
const possible_deployments = ['test', 'prod']

const streetAddressField = document.getElementById('streetAddress');
const countryField = document.getElementById('country');
const cityField = document.getElementById('city');
const suburbField = document.getElementById('suburb');
const postcodeField = document.getElementById('postcode');
const autocompleteList = document.getElementById('autocomplete-list');
const debounceTimeMs = 500;

let timer;
let previousInput = "";

/**
 * Update autocomplete when users input for street address field is changed.
 * Set debouncing time is used to avoid API overuse.
 * Also contains backend rate limiting in case of malicious users altering JS.
 */
streetAddressField.addEventListener('input', function() {
    clearTimeout(timer);
    timer = setTimeout(async function () {
        const inputValue = streetAddressField.value;
        if (inputValue) {
            if (inputValue !== previousInput) {
                previousInput = inputValue;
                await updateAutocomplete(inputValue, countryField.value);
            }
        } else {
            removeAutocompleteBox();
        }
    }, debounceTimeMs);
});

/**
 * When the input field is clicked in, if there is already text in there it will ask for autocomplete
 */
streetAddressField.addEventListener('focus', () => {
    clearTimeout(timer);
    timer = setTimeout(function() {
        const inputValue = streetAddressField.value;
        if (inputValue) {
            updateAutocomplete(inputValue, countryField.value);
        }
    }, debounceTimeMs);
});

/**
 * When user clicks outside the street address input the autocomplete box disappears
 */
streetAddressField.addEventListener('blur', function() {
    setTimeout(removeAutocompleteBox, 100)
})

/**
 * Call Java function to get the API response,
 * only if maximum number of requests in given rate limit timeframe has not yet been reached.
 *
 * @param query Input from street address search box.
 * @param country Input from country field, to find results only in specified country
 */
async function updateAutocomplete(query, country) {
    const deployment = window.location.pathname.split('/')[1];
    const baseUri = deployment !== undefined && possible_deployments.includes(deployment) ?`/${deployment}` : '';
    fetch(`${baseUri}/maptiler/search-results?query=${query}&country=${country}`)
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
 * Show the autocomplete box with the suggestions from API request.
 * Fill in any relevant fields if selection is made.
 *
 * @param data Raw data from API Request.
 */
function renderAutocomplete(data) {
    let dataList = data.locations;
    if (!dataList || dataList.length === 0) {
        console.log("No data, or maximum number of requests in set timeframe reached");
        removeAutocompleteBox();
        return;
    }
    autocompleteList.innerHTML = '';
    showAutocompleteBox()
    dataList.forEach(item => {
        let suggestionElement = document.createElement("div");
        suggestionElement.classList.add("dropdown-item");
        suggestionElement.classList.add("border-bottom");
        suggestionElement.classList.add("text-wrap");
        suggestionElement.style.cursor="pointer";
        let primaryTextElement = document.createElement("div");
        let secondaryTextElement = document.createElement("div", );
        primaryTextElement.classList.add("primary-text");
        secondaryTextElement.classList.add("secondary-text");

        let streetAddress = item.streetAddress;
        let outerLocation = item.outerLocation;
        let country = item.country;
        let city = item.city;
        let suburb = item.suburb;
        let postcode = item.postcode;

        let isStreetAddressPrimary = !!streetAddress;
        if (isStreetAddressPrimary) {
            primaryTextElement.innerHTML = streetAddress;
            secondaryTextElement.innerHTML = outerLocation;
        } else {
            primaryTextElement.innerHTML = outerLocation;
        }

        // Update input box on selection.
        suggestionElement.addEventListener('click', function() {
            streetAddressField.value = streetAddress;
            country ? countryField.value = country : countryField.value = "";
            city ? cityField.value = city : cityField.value = "";
            suburb ? suburbField.value = suburb : suburbField.value = "";
            postcode ? postcodeField.value = postcode : postcodeField.value = "";

            removeAutocompleteBox();
        });

        suggestionElement.appendChild(primaryTextElement);
        if (isStreetAddressPrimary) suggestionElement.appendChild(secondaryTextElement);
        autocompleteList.appendChild(suggestionElement);
    });
    const dropdownItems = document.getElementsByClassName("dropdown-item");
    dropdownItems.item(dropdownItems.length - 1).classList.remove("border-bottom");
}

function removeAutocompleteBox() {
    autocompleteList.innerHTML = '';
    autocompleteList.style.display = 'none';
}

function showAutocompleteBox() {
    autocompleteList.style.display = 'block';
}