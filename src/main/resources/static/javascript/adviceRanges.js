const adviceRanges = document.querySelectorAll(".advice-range");

const minTemp = document.getElementById("minTemp");
const maxTemp = document.getElementById("maxTemp");
const minSoilMoisture = document.getElementById("minSoilMoisture");
const maxSoilMoisture = document.getElementById("maxSoilMoisture");
const minAirPressure = document.getElementById("minAirPressure");
const maxAirPressure = document.getElementById("maxAirPressure");
const minHumidity = document.getElementById("minHumidity");
const maxHumidity = document.getElementById("maxHumidity");
const lightLevel = document.getElementById("lightLevel");

const adviceFormDataset = document.getElementById("garden-advice-ranges").dataset;
const openAdviceButton = document.getElementById("open-advice-button");

const errorMessages = document.querySelectorAll(".error.message");


document.addEventListener("DOMContentLoaded", function() {

    /**
     * Open modal if errors have occurred when submitting form.
     */
    if (adviceFormDataset.openModal === "true") {
        openAdviceButton.click();
    }

    /**
     * Reset values when form is reopened
     */
    openAdviceButton.addEventListener("click", function () {
        minTemp.value = adviceFormDataset.minTemp;
        maxTemp.value = adviceFormDataset.maxTemp;

        minSoilMoisture.value = adviceFormDataset.minMoisture;
        maxSoilMoisture.value = adviceFormDataset.maxMoisture;

        minAirPressure.value = adviceFormDataset.minPressure;
        maxAirPressure.value = adviceFormDataset.maxPressure;

        minHumidity.value = adviceFormDataset.minHumidity;
        maxHumidity.value = adviceFormDataset.maxHumidity;

        lightLevel.value = adviceFormDataset.lightLevel;

        errorMessages.forEach(function (errorMessage) {
            errorMessage.textContent = "";
        });

        adviceRanges.forEach(function (adviceRange) {
            adviceRange.classList.remove("border", "border-danger");
        })
    })
});

/**
 * Prevent form submitting on user pressing Enter
 */
adviceRanges.forEach((input) => {
    input.addEventListener("keydown", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
        }
    });
});

/**
 * Prevent invalid characters and invalid sizes.
 */
function preventInvalidCharacters(event) {
    const key = event.key;
    const allowedKeys = [
        'Backspace', 'Tab', 'ArrowLeft', 'ArrowRight', 'ArrowUp', 'ArrowDown',
        'Delete', 'Home', 'End', 'Enter', '.', '-'
    ];
    if (!/^\d$/.test(key) && !allowedKeys.includes(key)) {
        event.preventDefault();
    }
}

for (const adviceRange of adviceRanges) {
    adviceRange.addEventListener('keydown', function(event) {
        preventInvalidCharacters(event);
    });
}

/**
 * Ensure the minimum is the minimum and the maximum is the maximum for advice ranges
 * @param minElement Input Min Element
 * @param maxElement Input Max Element
 */
function ensureMinMaxCorrect(minElement, maxElement) {
    // In min max range
    minElement.addEventListener("input", function(event) {
        const value = parseFloat(event.target.value);
        const maxValue = parseFloat(maxElement.value);
        if (value >= maxValue || !valueInMinMaxRange(maxElement, value)) {
            minElement.classList.add("border", "border-danger");
        } else {
            minElement.classList.remove("border", "border-danger");
            if (valueInMinMaxRange(maxElement, maxValue) && maxValue > value) {
                maxElement.classList.remove("border", "border-danger");
            }
        }
    });
    maxElement.addEventListener("input", function(event) {
        const value = parseFloat(event.target.value)
        const minValue = parseFloat(minElement.value);
        if (value <= minValue || !valueInMinMaxRange(maxElement, value)) {
            maxElement.classList.add("border", "border-danger");
        } else {
            maxElement.classList.remove("border", "border-danger");
            if (valueInMinMaxRange(maxElement, minValue) && minValue < value) {
                minElement.classList.remove("border", "border-danger");
            }
        }
    })
}

document.getElementById("resetAdviceRangesButton").addEventListener('click', () => {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch('monitor/reset', {
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken
        }
    })
        .then(() => location.reload())
        .catch(() => null);
})

function valueInMinMaxRange(element, value) {
    return value >= element.min && value <= element.max;
}

ensureMinMaxCorrect(minTemp, maxTemp);
ensureMinMaxCorrect(minHumidity, maxHumidity);
ensureMinMaxCorrect(minAirPressure, maxAirPressure);
ensureMinMaxCorrect(minSoilMoisture, maxSoilMoisture);
