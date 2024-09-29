const plantSuggestionsButton = document.getElementById("plant-suggestions-button");
const plantSuggestionContainer = document.getElementById("plant-suggestion-container");

plantSuggestionsButton.addEventListener("click", function () {
    console.log("PLANT SUGGESTIONS CLICKED");
    //TODO add loading stuff as suggestion takes a while
    fetchPlantSuggestions(plantSuggestionsButton.dataset.gardenId);
});


function fetchPlantSuggestions(gardenId) {
    const deployment = window.location.pathname.split('/')[1];
    const baseUri = deployment !== undefined && possible_deployments.includes(deployment) ?`/${deployment}` : '';
    fetch( `${baseUri}/ai/suggestions?gardenId=${gardenId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            renderPlantSuggestions(data);
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}


function renderPlantSuggestions(plantSuggestionList) {
    console.log(plantSuggestionList);
    plantSuggestionContainer.innerHTML = '';
    if (!plantSuggestionList) {
        const itemElement = document.createElement("p");
        itemElement.innerHTML = "No Suggestions found";
        plantSuggestionContainer.appendChild(itemElement);
        return;
    } else if (plantSuggestionList.size != 3) {
        const itemElement = document.createElement("p");
        itemElement.innerHTML = plantSuggestionList[0];
        plantSuggestionContainer.appendChild(itemElement);
        return;
    }
    for (const plantSuggestion of plantSuggestionList) {
        const itemElement = document.createElement("p");
        itemElement.innerHTML = plantSuggestion;
        plantSuggestionContainer.appendChild(itemElement);
    }
}

