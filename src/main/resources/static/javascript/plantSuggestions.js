const plantSuggestionsButton = document.getElementById("plant-suggestions-button");
const plantSuggestionContainer = document.getElementById("plant-suggestion-container");
const spinner = document.getElementById("suggestion-on-load");
plantSuggestionsButton.addEventListener("click", function () {
    console.log("PLANT SUGGESTIONS CLICKED");
    fetchPlantSuggestions(plantSuggestionsButton.dataset.gardenId);
});


function fetchPlantSuggestions(gardenId) {
    const deployment = window.location.pathname.split('/')[1];
    const baseUri = deployment !== undefined && possible_deployments.includes(deployment) ?`/${deployment}` : '';
    spinner.style.display = "inline-block";
    fetch( `${baseUri}/ai/suggestions?gardenId=${gardenId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            spinner.style.display = "none";
            renderPlantSuggestions(data);
        })
        .catch(error => {
            spinner.style.display = "none";
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