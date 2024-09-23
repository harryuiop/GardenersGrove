const temperatureGraphContainer = document.getElementById("graphs");

const graphDataSet = document.getElementById("display-graphs").dataset;
const monthLabels = JSON.parse(graphDataSet.monthLabels);
const dayLabels = JSON.parse(graphDataSet.dayLabels);

const GRAPH_COLOR = 'rgb(75, 192, 192)';

const GraphType = Object.freeze({
    MONTH: 0, DAY: 2
});

let monthGraph, weekGraph, dayGraph;
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


function compareMonthGraphs(sensorName, data1, data2, timeLabels) {
    return [
        {
            labels: timeLabels,
            datasets: [{
                label: `Garden 1 Average ${sensorName} per Day`,
                data: data1,
                fill: false,
                borderColor: GRAPH_COLOR,
                tension: 0.1
            }, {
                label: `Garden 2 Average ${sensorName} per Day`,
                data: data2,
                fill: false,
                borderColor: 'rgb(52, 152, 219)',
                tension: 0.1
            }]
        }, "Time (Day)", sensorName]
}

function compareDayGraphs(sensorName, data1, data2, timeLabels) {
    return [
        {
            labels: timeLabels,
            datasets: [{
                label: `Garden 1 Average ${sensorName} per Day`,
                data: data1,
                fill: false,
                borderColor: GRAPH_COLOR,
                tension: 0.1
            }, {
                label: `Garden 2 Average ${sensorName} per Day`,
                data: data2,
                fill: false,
                borderColor: 'rgb(52, 152, 219)',
                tension: 0.1
            }]
        }, "Time (Half-hourly)", sensorName]
}

function renderTemperatureCompareGraph(){
    const tempMonthResults = JSON.parse(graphDataSet.monthTemp);
    const tempDayResults = JSON.parse(graphDataSet.dayTemp);
    const tempMonthResultsOther = JSON.parse(graphDataSet.monthTempOther);
    const tempDayResultsOther = JSON.parse(graphDataSet.dayTempOther);

    monthGraph = createGraph(tempMonthResults, tempMonthResultsOther, "graph-month", "Temperature",
        GraphType.MONTH, monthLabels);
    dayGraph = createGraph(tempDayResults, tempDayResultsOther, "graph-day", "Temperature",
        GraphType.DAY, dayLabels)
}

function renderMoistureCompareGraph() {
    const moistureMonthResults = JSON.parse(graphDataSet.monthMoisture)
    const moistureDayResults = JSON.parse(graphDataSet.dayMoisture)
    const moistureMonthResultsOther = JSON.parse(graphDataSet.monthMoistureOther)
    const moistureDayResultsOther = JSON.parse(graphDataSet.dayMoistureOther)

    monthGraph = createGraph(moistureMonthResults, moistureMonthResultsOther, "graph-month", "Moisture",
        GraphType.MONTH, monthLabels);
    dayGraph = createGraph(moistureDayResults, moistureDayResultsOther, "graph-day", "Moisture",
        GraphType.DAY, dayLabels)
}

function renderLightCompareGraph() {
    const lightMonthResults = JSON.parse(graphDataSet.monthLight)
    const lightDayResults = JSON.parse(graphDataSet.dayLight)
    const lightMonthResultsOther = JSON.parse(graphDataSet.monthLightOther)
    const lightDayResultsOther = JSON.parse(graphDataSet.dayLightOther)

    monthGraph = createGraph(lightMonthResults, lightMonthResultsOther, "graph-month", "Light",
        GraphType.MONTH, monthLabels)
    dayGraph = createGraph(lightDayResults, lightDayResultsOther, "graph-day", "Light",
        GraphType.DAY, dayLabels)
}

function renderPressureCompareGraph() {
    const pressureMonthResults = JSON.parse(graphDataSet.monthPressure)
    const pressureDayResults = JSON.parse(graphDataSet.dayPressure)
    const pressureMonthResultsOther = JSON.parse(graphDataSet.monthPressureOther)
    const pressureDayResultsOther = JSON.parse(graphDataSet.dayPressureOther)

    monthGraph = createGraph(pressureMonthResults, pressureMonthResultsOther, "graph-month", "Pressure",
        GraphType.MONTH, monthLabels)
    dayGraph = createGraph(pressureDayResults, pressureDayResultsOther, "graph-day", "Pressure",
        GraphType.DAY, dayLabels)
}

function renderHumidityCompareGraph() {
    const humidityMonthResults = JSON.parse(graphDataSet.monthHumidity)
    const humidityDayResults = JSON.parse(graphDataSet.dayHumidity)
    const humidityMonthResultsOther = JSON.parse(graphDataSet.monthHumidityOther)
    const humidityDayResultsOther = JSON.parse(graphDataSet.dayHumidityOther)

    monthGraph = createGraph(humidityMonthResults, humidityMonthResultsOther, "graph-month", "Humidity",
        GraphType.MONTH, monthLabels)
    dayGraph = createGraph(humidityDayResults, humidityDayResultsOther, "graph-day", "Humidity",
        GraphType.DAY, dayLabels)
}

function createGraph(data1, data2, graphId, sensorName, graphType, timeLabels) {
    if (!data1 || data1.length === 0){
        console.error("No data points given for data:", data1);
        return;
    }

    // Get graph information
    switch (graphType) {
        case GraphType.DAY:
            [dataObject, xLabel, yLabel] = compareDayGraphs(sensorName, data1, data2, timeLabels);
            break;
        case GraphType.MONTH:
            [dataObject, xLabel, yLabel] = compareMonthGraphs(sensorName, data1, data2, timeLabels);
            break;
        default:
            [dataObject, xLabel, yLabel] = compareDayGraphs(sensorName, data1, data2, timeLabels);
    }

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