const graphDataSet = document.getElementById("display-graphs").dataset;
const monthLabels = JSON.parse(graphDataSet.monthLabels);
const dayLabels = JSON.parse(graphDataSet.dayLabels);

const garden1 = document.getElementById("garden-names").dataset.yourGarden;
const garden2 = document.getElementById("garden-names").dataset.theirGarden;

const GARDEN_1_COLOR = 'rgb(75, 192, 192)';
const GARDEN_2_COLOR = 'rgb(52, 152, 219)';

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
    const tempMonthResults = JSON.parse(graphDataSet.monthTemp);
    const tempDayResults = JSON.parse(graphDataSet.dayTemp);
    const tempMonthResultsOther = JSON.parse(graphDataSet.monthTempOther);
    const tempDayResultsOther = JSON.parse(graphDataSet.dayTempOther);

    monthGraph = createGraph(compareMonthGraphs("Temperature", tempMonthResults, tempMonthResultsOther),"graph-compare-month")
    dayGraph = createGraph(compareDayGraphs("Temperature", tempDayResults, tempDayResultsOther),"graph-compare-day")
}

function renderMoistureCompareGraph() {
    const moistureMonthResults = JSON.parse(graphDataSet.monthMoisture)
    const moistureDayResults = JSON.parse(graphDataSet.dayMoisture)
    const moistureMonthResultsOther = JSON.parse(graphDataSet.monthMoistureOther)
    const moistureDayResultsOther = JSON.parse(graphDataSet.dayMoistureOther)

    monthGraph = createGraph(compareMonthGraphs("Moisture", moistureMonthResults, moistureMonthResultsOther), "graph-compare-month")
    dayGraph = createGraph(compareDayGraphs("Moisture", moistureDayResults, moistureDayResultsOther),"graph-compare-day")
}

function renderLightCompareGraph() {
    const lightMonthResults = JSON.parse(graphDataSet.monthLight)
    const lightDayResults = JSON.parse(graphDataSet.dayLight)
    const lightMonthResultsOther = JSON.parse(graphDataSet.monthLightOther)
    const lightDayResultsOther = JSON.parse(graphDataSet.dayLightOther)

    monthGraph = createGraph(compareMonthGraphs("Light", lightMonthResults, lightMonthResultsOther), "graph-compare-month")
    dayGraph = createGraph(compareDayGraphs("Light", lightDayResults, lightDayResultsOther), "graph-compare-day")
}

function renderPressureCompareGraph() {
    const pressureMonthResults = JSON.parse(graphDataSet.monthPressure)
    const pressureDayResults = JSON.parse(graphDataSet.dayPressure)
    const pressureMonthResultsOther = JSON.parse(graphDataSet.monthPressureOther)
    const pressureDayResultsOther = JSON.parse(graphDataSet.dayPressureOther)

    monthGraph = createGraph(compareMonthGraphs("Pressure", pressureMonthResults, pressureMonthResultsOther), "graph-compare-month")
    dayGraph = createGraph(compareDayGraphs("Pressure", pressureDayResults, pressureDayResultsOther), "graph-compare-day")
}

function renderHumidityCompareGraph() {
    const humidityMonthResults = JSON.parse(graphDataSet.monthHumidity)
    const humidityDayResults = JSON.parse(graphDataSet.dayHumidity)
    const humidityMonthResultsOther = JSON.parse(graphDataSet.monthHumidityOther)
    const humidityDayResultsOther = JSON.parse(graphDataSet.dayHumidityOther)

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