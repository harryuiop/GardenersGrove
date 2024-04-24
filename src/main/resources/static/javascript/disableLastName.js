function updateCheckboxValue() {
    var checkbox = document.getElementById("noSurname");
    var lastName = document.getElementById("lastName")
    if (checkbox.checked) {
        checkbox.value = "true";
        lastName.value = "";
        lastName.disabled=true;
    } else {
        checkbox.value = "false";
        lastName.disabled=false;
    }
}

function loadingPage() {
    var checkbox = document.getElementById("noSurname");
    var lastName = document.getElementById("lastName")
    if (checkbox.value === "true") {
        checkbox.checked = true;
        lastName.value = "";
        lastName.disabled=true;
    } else {
        checkbox.value = "false";
        lastName.disabled=false;
    }
}