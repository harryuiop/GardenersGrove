const searchBarField = document.getElementById('streetAddress');

searchBarField.addEventListener('input', function() {
    const inputValue = searchBarField.value;
    if (inputValue) {
        if (inputValue !== previousInput) {
            previousInput = inputValue;
            updateAutocomplete(inputValue, countryField.value);
        }
    } else {
        removeAutocompleteBox();
    }
});



function removeAutocompleteBox() {
    autocompleteList.innerHTML = '';
    autocompleteList.style.display = 'none';
}

function showAutocompleteBox() {
    autocompleteList.style.display = 'block';
}
