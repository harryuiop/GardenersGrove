const alertSensor = document.getElementById("sensor-alert");
const advicePopup =     document.getElementById("advice");

// Advice Reference
const referenceLink = document.querySelector('[data-bs-target="#referenceModal"]');
const referenceListElement = document.getElementById("reference-list");
const referencesDataset = document.getElementById('references').dataset;

let sensorAdviceMessageDisabled = new Map();


/**
 * Show the correct alert and advice message for the given sensor.
 *
 * @param {string} sensor The particular sensor metric to show alert and advice for.
 */
function alertMessage(sensor) {
    if (isNaN(Number(disconnectedWarning.getAttribute("data-"+sensor.toLowerCase())))) {
        disconnectedWarning.style.display = "block";
        alertSensor.innerText = sensor;
    } else {
        disconnectedWarning.style.display = "none";
    }

    const adviceMessage = advicePopup.getAttribute("data-"+sensor.toLowerCase());
    if (adviceMessage != null && !sensorAdviceMessageDisabled.get(sensor)) {
        advicePopup.style.display = "block";
        advicePopup.firstChild.textContent = adviceMessage;
    } else {
        advicePopup.style.display = "none";
    }
}

/**
 * hide the advice message for the currently selected sensor.
 */
function closeAdvicePopup() {
    sensorAdviceMessageDisabled.set(currentlySelectedSensorView, true);
    advicePopup.style.display = "none";
}

referenceLink.addEventListener('click', function(e) {
    // Prevent the default action of the link
    e.preventDefault();

    // Update the modal content based on the advice
    showReference();
})

/**
 * Loads the corresponding references in the modal depending on the advice message
 */
function showReference() {
    switch (currentlySelectedSensorView){
        case "Temperature":
            addRefToHTML(referencesDataset.temperatureRef);
            break;
        case "Moisture":
            addRefToHTML(referencesDataset.moistureRef);
            break;
        case "Light":
            addRefToHTML(referencesDataset.lightRef);
            break;
        case "Humidity":
            addRefToHTML(referencesDataset.humidityRef);
            break;
        default:
            console.error("References cannot be loaded with this sensor id");
    }
}

/**
 * Create reference list elements and add to modal.
 *
 * @param rawReferenceData String representation of referenceData to be converted in a
 * key, value object
 */
const addRefToHTML = (rawReferenceData) => {
    let referenceData = JSON.parse("{" + rawReferenceData.substring(1, rawReferenceData.length - 1) + "}");
    referenceListElement.innerHTML = "";
    Object.entries(referenceData).forEach(([websiteName, url]) => {
        const listAttribute = document.createElement("li");
        const linkElement = document.createElement("a");
        linkElement.href = url;
        linkElement.innerHTML = websiteName;
        listAttribute.appendChild(linkElement);
        referenceListElement.appendChild(listAttribute)
    });
}