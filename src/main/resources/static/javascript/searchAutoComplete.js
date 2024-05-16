var modal = document.getElementById("confirm-send-request")
const searchField = document.getElementById('searchUsers')
const autocompleteList = document.getElementById('autocomplete-list');
const debounceTimeMs = 50;
var requestedUser;

let timer;
let previousInput = "";
searchField.addEventListener('input', function() {
    clearTimeout(timer);
    timer = setTimeout(function() {
        const inputValue = searchField.value;
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

function updateAutocomplete(searchString) {
    fetch(`/search?searchUser=${searchString}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        }).then(data => {
            renderAutocomplete(data)
    }).catch(error => {
        console.error("Oops: ", error)
    });
}

function renderAutocomplete(users) {
    if (!users || users.length == 0) {
        return;
    }
    autocompleteList.innerHTML = '';
    showAutocompleteBox()
    users.forEach(user => {
        let suggestionElement = document.createElement("div");
        let primaryTextElement = document.createElement("div");
        primaryTextElement.classList.add("primary-text")

        primaryTextElement.innerHTML = user;

        suggestionElement.addEventListener('click', function () {
            sendRequest(user)
        });

        suggestionElement.appendChild(primaryTextElement);
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

function sendRequest(user) {
    var text = document.getElementById("request-user")
    requestedUser = user
    text.textContent = `Would you like to request ${user} as a friend?`
    modal.style.display="block"
}

function closeModal() {
    modal.style.display="none"
}

function makeFriends() {
    fetch(`/send/request?email=${requestedUser}`)
        .then(response => {

        }).catch(error => {
            colsole.error('Error: ', error)
    })
    closeModal()
}