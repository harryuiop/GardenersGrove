const tempUnits = document.getElementById('temp-units');
const fahrenheitButton = document.getElementById("fahrenheit-btn");
const celsiusButton = document.getElementById("celsius-btn");
let currentUnit = 'C';

const graphDataSet = document.getElementById("display-graphs").dataset;
const monthLabels = JSON.parse(graphDataSet.monthLabels);
const dayLabels = JSON.parse(graphDataSet.dayLabels);

const garden1 = document.getElementById("garden-names").dataset.yourGarden;
const garden2 = document.getElementById("garden-names").dataset.theirGarden;

const GARDEN_1_COLOR = 'rgb(71, 118, 65)';
const GARDEN_2_COLOR = 'rgb(20, 41, 21)';

let monthGraph, dayGraph;
let currentlySelectedSensorView = "Temperature";

/**
 * Render temperature graphs on page load.
 */
window.onload = function() {
    renderTemperatureCompareGraph();
}

/**
 * Make graphs for that sensor shown.
 * @param {string} buttonId Sensor selected
 */
function makeActive(buttonId) {
    const allButtons = ['Temperature', 'Moisture', 'Light', 'Pressure', 'Humidity']
    const button = document.getElementById(buttonId);

    button.classList = 'btn btn-stats-bar-active btn-no-bold m-0 lead';

    allButtons.forEach( allButtonsItem => {
            if (allButtonsItem !== buttonId) {
                document.getElementById(allButtonsItem).classList = 'btn btn-stats-bar btn-no-bold m-0 lead'
            }
        }
    )

    currentlySelectedSensorView = buttonId;


    switch (buttonId){
        case "Temperature":
            renderTemperatureCompareGraph();
            break;
        case "Moisture":
            renderMoistureCompareGraph();
            break;
        case "Light":
            renderLightCompareGraph();
            break;
        case "Pressure":
            renderPressureCompareGraph();
            break;
        case "Humidity":
            renderHumidityCompareGraph();
            break;
        default:
            console.error("Graph button id is invalid");
    }
}

/**
 * Parse data results (day, week, month) into a 2dp javascript array
 *
 * @param resultArrayString String representation of dataset passed via thymeleaf
 * @returns 2dp javascript array
 */
function parseDataResults(resultArrayString) {
    return JSON.parse(resultArrayString).map(reading =>
        reading == null ? reading : reading.toFixed(2));
}


function compareMonthGraphs(sensorName, data1, data2) {
    return [
        {
            labels: monthLabels,
            datasets: [{
                label: `${garden1} Average ${sensorName} per Day`,
                data: data1,
                fill: false,
                borderColor: GARDEN_1_COLOR,
                tension: 0.1
            }, {
                label: `${garden2} Average ${sensorName} per Day`,
                data: data2,
                fill: false,
                borderColor: GARDEN_2_COLOR,
                tension: 0.1
            }]
        }, "Time (Day)", sensorName]
}

function compareDayGraphs(sensorName, data1, data2) {
    return [
        {
            labels: dayLabels,
            datasets: [{
                label: `${garden1} Average ${sensorName} per Day`,
                data: data1,
                fill: false,
                borderColor: GARDEN_1_COLOR,
                tension: 0.1
            }, {
                label: `${garden2} Average ${sensorName} per Day`,
                data: data2,
                fill: false,
                borderColor: GARDEN_2_COLOR,
                tension: 0.1
            }]
        }, "Time (Half-hourly)", sensorName]
}

function renderTemperatureCompareGraph(){
    tempUnits.style.display = "block";

    const tempMonthResults = parseDataResults(graphDataSet.monthTemp);
    const tempDayResults = parseDataResults(graphDataSet.dayTemp);
    const tempMonthResultsOther = parseDataResults(graphDataSet.monthTempOther);
    const tempDayResultsOther = parseDataResults(graphDataSet.dayTempOther);

    const isCelsius = currentUnit === 'C';

    const convertedMonthResults = isCelsius ? tempMonthResults : tempMonthResults.map(convertCelsiusToFahrenheit);
    const convertedMonthResultsOther = isCelsius ? tempMonthResultsOther : tempMonthResultsOther.map(convertCelsiusToFahrenheit);
    const convertedDayResults = isCelsius ? tempDayResults : tempDayResults.map(convertCelsiusToFahrenheit);
    const convertedDayResultsOther = isCelsius ? tempDayResultsOther : tempDayResultsOther.map(convertCelsiusToFahrenheit);

    monthGraph = createGraph(compareMonthGraphs("Temperature", convertedMonthResults, convertedMonthResultsOther),"graph-compare-month")
    dayGraph = createGraph(compareDayGraphs("Temperature", convertedDayResults, convertedDayResultsOther),"graph-compare-day")
}

function renderMoistureCompareGraph() {
    tempUnits.style.display = "none";

    const moistureMonthResults = parseDataResults(graphDataSet.monthMoisture)
    const moistureDayResults = parseDataResults(graphDataSet.dayMoisture)
    const moistureMonthResultsOther = parseDataResults(graphDataSet.monthMoistureOther)
    const moistureDayResultsOther = parseDataResults(graphDataSet.dayMoistureOther)

    monthGraph = createGraph(compareMonthGraphs("Moisture", moistureMonthResults, moistureMonthResultsOther), "graph-compare-month")
    dayGraph = createGraph(compareDayGraphs("Moisture", moistureDayResults, moistureDayResultsOther),"graph-compare-day")
}

function renderLightCompareGraph() {
    tempUnits.style.display = "none";

    const lightMonthResults = parseDataResults(graphDataSet.monthLight)
    const lightDayResults = parseDataResults(graphDataSet.dayLight)
    const lightMonthResultsOther = parseDataResults(graphDataSet.monthLightOther)
    const lightDayResultsOther = parseDataResults(graphDataSet.dayLightOther)

    monthGraph = createGraph(compareMonthGraphs("Light", lightMonthResults, lightMonthResultsOther), "graph-compare-month")
    dayGraph = createGraph(compareDayGraphs("Light", lightDayResults, lightDayResultsOther), "graph-compare-day")
}

function renderPressureCompareGraph() {
    tempUnits.style.display = "none";

    const pressureMonthResults = parseDataResults(graphDataSet.monthPressure)
    const pressureDayResults = parseDataResults(graphDataSet.dayPressure)
    const pressureMonthResultsOther = parseDataResults(graphDataSet.monthPressureOther)
    const pressureDayResultsOther = parseDataResults(graphDataSet.dayPressureOther)

    monthGraph = createGraph(compareMonthGraphs("Pressure", pressureMonthResults, pressureMonthResultsOther), "graph-compare-month")
    dayGraph = createGraph(compareDayGraphs("Pressure", pressureDayResults, pressureDayResultsOther), "graph-compare-day")
}

function renderHumidityCompareGraph() {
    tempUnits.style.display = "none";

    const humidityMonthResults = parseDataResults(graphDataSet.monthHumidity)
    const humidityDayResults = parseDataResults(graphDataSet.dayHumidity)
    const humidityMonthResultsOther = parseDataResults(graphDataSet.monthHumidityOther)
    const humidityDayResultsOther = parseDataResults(graphDataSet.dayHumidityOther)

    monthGraph = createGraph(compareMonthGraphs("Humidity", humidityMonthResults, humidityMonthResultsOther), "graph-compare-month")
    dayGraph = createGraph(compareDayGraphs("Humidity", humidityDayResults, humidityDayResultsOther), "graph-compare-day")
}

/**
 * Uses data to create a graph which is generated and displayed in given id
 * @param data          data points for the graph
 * @param graphId       div where the graph is made
 */
function createGraph([dataObject, xLabel, yLabel], graphId) {
    return new Chart(document.getElementById(graphId),
        {
            type: 'line',
            data: dataObject,
            options: {
                responsive: true,
                maintainAspectRatio: true,
                aspectRatio: 1.25,
                scales: {
                    yAxes: [{
                        scaleLabel: {
                            display: true,
                            labelString: yLabel
                        }
                    }],
                    xAxes: [{
                        scaleLabel: {
                            display: true,
                            labelString: xLabel
                        }
                    }]
                },
            }
        }
    )
}

function changeTemperatureUnit(unit) {
    if (unit === 'c') {
        celsiusButton.className = "btn btn-toggle-selected";
        fahrenheitButton.className = "btn btn-toggle-unselected";
        currentUnit = 'C'

    } else {
        celsiusButton.className = "btn btn-toggle-unselected";
        fahrenheitButton.className = "btn btn-toggle-selected";
        currentUnit = 'F'
    }
    renderTemperatureCompareGraph();
}

function convertCelsiusToFahrenheit(celsiusInput) {
    if (celsiusInput === null) return null;
    return celsiusInput * 1.8 + 32;
}
