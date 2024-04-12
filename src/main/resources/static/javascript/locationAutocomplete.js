const streetAddressField = document.getElementById('streetAddress');
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
            console.log(data); // Handle the response data
            renderAutocomplete(data);
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}

function renderAutocomplete(data) {
    autocompleteList.innerHTML = '';
    let dataList = data.name
    showAutocompleteBox()
    dataList.forEach(item => {
        console.log("ac: " + item);
        const suggestionElement = document.createElement('div');
        suggestionElement.textContent = item;
        suggestionElement.addEventListener('click', function() {
            streetAddressField.value = item.name;
            removeAutocompleteBox();
        });
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