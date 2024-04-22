const streetAddressField = document.getElementById('streetAddress');
const countryField = document.getElementById('country');
const cityField = document.getElementById('city');
const suburbField = document.getElementById('suburb');
const postcodeField = document.getElementById('postcode');
const autocompleteList = document.getElementById('autocomplete-list');
const debounceTimeMs = 500;

let timer;
let previousInput = "";
streetAddressField.addEventListener('input', function() {
    clearTimeout(timer);
    timer = setTimeout(function() {
        const inputValue = streetAddressField.value;
        if (inputValue) {
            if (inputValue !== previousInput) {
                previousInput = inputValue;
                updateAutocomplete(inputValue, countryField.value);
            }
        } else {
            removeAutocompleteBox();
        }
    }, debounceTimeMs);
});

/**
 * Call Java function to get the API response,
 * only if maximum number of requests in given rate limit timeframe has not yet been reached.
 *
 * @param query Input from street address search box.
 * @param country Input from country field, to find results only in specified country
 */
function updateAutocomplete(query, country) {
    fetch(`/maptiler/search-results?query=${query}&country=${country}`)
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
        return;
    }
    autocompleteList.innerHTML = '';
    showAutocompleteBox()
    dataList.forEach(item => {
        let suggestionElement = document.createElement("div");
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

        let isStreetAddressPrimary = streetAddress ? true : false;
        if (isStreetAddressPrimary) {
            primaryTextElement.innerHTML = streetAddress;
            secondaryTextElement.innerHTML = outerLocation;
        } else {
            primaryTextElement.innerHTML = outerLocation;
        }

        // Update input box on selection.
        suggestionElement.addEventListener('click', function() {
            streetAddressField.value = streetAddress;
            if (country) countryField.value = country;
            if (city) cityField.value = city;
            if (suburb) suburbField.value = suburb;
            if (postcode) postcodeField.value = postcode

            removeAutocompleteBox();
        });

        suggestionElement.appendChild(primaryTextElement);
        if (isStreetAddressPrimary) suggestionElement.appendChild(secondaryTextElement);
        autocompleteList.appendChild(suggestionElement);
    });
}

function removeAutocompleteBox() {
    autocompleteList.innerHTML = '';
    autocompleteList.style.display = 'none';
}

function showAutocompleteBox() {
    autocompleteList.style.display = 'block';
}