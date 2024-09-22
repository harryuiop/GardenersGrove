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
            adviceRanges[index].classList.remove("border", "border-danger");
        });
    }
})

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

function valueInMinMaxRange(element, value) {
    return value >= element.min && value <= element.max;
}

ensureMinMaxCorrect(minTemp, maxTemp);
ensureMinMaxCorrect(minHumidity, maxHumidity);
ensureMinMaxCorrect(minAirPressure, maxAirPressure);
ensureMinMaxCorrect(minSoilMoisture, maxSoilMoisture);
