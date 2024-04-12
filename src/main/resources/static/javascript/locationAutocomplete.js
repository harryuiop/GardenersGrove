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
                updateAutocomplete(inputValue);
            }
        } else {
            removeAutocompleteBox();
        }
    }, debounceTimeMs);
});

function updateAutocomplete(query) {
    fetch(`/maptiler/searchresults?query=${query}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            renderAutocomplete(data);
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}

function renderAutocomplete(data) {
    autocompleteList.innerHTML = '';
    let dataList = data.locations;
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