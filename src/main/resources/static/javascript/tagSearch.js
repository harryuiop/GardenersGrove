/**
 * Tag Search, handles tags being added to search.
 * Note: Depends on define elements in tagAutocomplete
 */

const tagContainer = document.getElementById("tags-container");
const tagErrorMessage = document.getElementById("tag-error-message");

/**
 * On user adding tag, queries database if tag exists via rest request.
 * TODO: Handle enter error (submits entire form).
 */
function submitTagToSearch() {
    const addedTag = tagField.value;

    const deployment = window.location.pathname.split('/')[1];
    const baseUri = deployment !== undefined && possible_deployments.includes(deployment) ?`/${deployment}` : '';
    fetch( `${baseUri}/tag/exists?query=${addedTag}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(tagExists => {
            handleTagSearchSubmit(tagExists, addedTag);
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}

/**
 * Handle a tag being submitted
 * @param tagExists If tag exists
 * @param input Tag name
 */
function handleTagSearchSubmit(tagExists, input) {
    if (tagExists) {
        tagField.value = "";
        removeTagError();
        removeAutocompleteBox();
        addTagToSearch(input);
    } else {
        tagErrorMessage.innerText = "No tag matching " + input;
        tagField.classList.add("border",  "border-danger");
    }

}

/**
 * Remove error message, and box highlight.
 */
function removeTagError() {
    tagErrorMessage.innerText = ""
    tagField.classList.remove("border",  "border-danger");
}

/**
 * Add Tag to search container
 * @param tag Tag text to add
 */
function addTagToSearch(tag) {
    if (tagContainer.children.length === 0) {
        let tagTitle = document.createElement("span");
        tagTitle.innerHTML = "Tag Selection:";
        tagContainer.appendChild(tagTitle);
    }
    let tagElement = document.createElement("span");
    tagElement.innerHTML = "&nbsp;&nbsp; #" + tag;
    tagContainer.appendChild(tagElement);
}




