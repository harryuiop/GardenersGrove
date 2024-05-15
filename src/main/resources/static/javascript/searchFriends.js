function searchUsers() {
    var searchString = document.getElementById("searchUsersBar")
    console.log(searchString.value)

    // var autocompleteList = document.getElementById("autocomplete-list")
    fetch(`/search?search=${searchString.value}`).then()
}