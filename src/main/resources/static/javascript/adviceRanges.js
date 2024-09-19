const adviceRanges = document.querySelectorAll(".advice-range");

const openAdviceButton = document.getElementById("open-advice-button");

const minTemp = document.getElementById("minTemp");
const maxTemp = document.getElementById("maxTemp");
const minSoilMoisture = document.getElementById("minSoilMoisture");
const maxSoilMoisture = document.getElementById("maxSoilMoisture");
const minAirPressure = document.getElementById("minAirPressure");
const maxAirPressure = document.getElementById("maxAirPressure");
const minHumidity = document.getElementById("minHumidity");
const maxHumidity = document.getElementById("maxHumidity");

/**
 * Maintain saved initial advice range values.
 */
let initialValues;
document.addEventListener("DOMContentLoaded", function() {
    initialValues = Array.from(adviceRanges).map(input => input.value);
});
openAdviceButton.addEventListener("click", function () {
    if (initialValues) {
        initialValues.forEach((value, index) => {
            adviceRanges[index].value = value;
        });
    }
})

/**
 * Prevent invalid characters and invalid sizes.
 */
function preventInvalidCharacters(event) {
    if (['e', 'E', '+'].includes(event.key)) {
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
    minElement.addEventListener("input", function(event) {
        if (event.target.value >= parseFloat(maxElement.value)) {
            minElement.classList.add("border", "border-danger");
        } else {
            minElement.classList.remove("border", "border-danger");
            maxElement.classList.remove("border", "border-danger");
        }
    });
    maxElement.addEventListener("input", function(event) {
        if (event.target.value <= parseFloat(maxElement.value)) {
            maxElement.classList.add("border", "border-danger");
        } else {
            maxElement.classList.remove("border", "border-danger");
            minElement.classList.remove("border", "border-danger");
        }
    })
}

ensureMinMaxCorrect(minTemp, maxTemp);
ensureMinMaxCorrect(minHumidity, maxHumidity);
ensureMinMaxCorrect(minAirPressure, maxAirPressure);
ensureMinMaxCorrect(minSoilMoisture, maxSoilMoisture);
