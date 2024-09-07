/**
 * Generator and manager for Graphs for all sensors in garden monitoring page.
 */

const fahrenheitButton = document.getElementById("fahrenheit-btn");
const celsiusButton = document.getElementById("celsius-btn");

// Containers
const temperatureGraphContainer = document.getElementById("temperature-graphs");

// Data
const tempMonthResults = JSON.parse(document.getElementById("temp-graph-month").dataset.results);
const tempWeeklyResults = JSON.parse(document.getElementById("temp-graph-week").dataset.results);
const tempDayResults = JSON.parse(document.getElementById("temp-graph-day").dataset.results);
console.log("Temperature data:", tempMonthResults);

// Labels
const labelDataSet = document.getElementById("display-graphs").dataset;
const monthLabels = JSON.parse(labelDataSet.monthLabels);
const dayLabels = JSON.parse(labelDataSet.dayLabels);
const weekLabels = JSON.parse(labelDataSet.weekLabels);

const GRAPH_COLOR = 'rgb(75, 192, 192)';
const WEEK_GRAPH_COLORS = ['rgb(44, 62, 80)', 'rgb(241, 196, 15)', 'rgb(52, 152, 219)', 'rgb(231, 76, 60)'];

// Graph Type enum, used for labelling.
const GraphType = Object.freeze({
    MONTH: 0, WEEK: 1, DAY: 2
});

/**
 * Render all graphs on page load.
 * Manage what graphs are shown / hidden.
 */
window.onload = function() {
    renderTemperatureGraphs();
    renderPressureGraph();
}

/**
 * Change temperature unit to Fahrenheit or Celsius AND update graph
 * @param unit c for Celsius, anything else for Fahrenheit
 */
function changeTemperatureUnit(unit) {
    if (unit === 'c') {
        celsiusButton.className = "btn btn-toggle-selected";
        fahrenheitButton.className = "btn btn-toggle-unselected";
        temperatureGraphContainer.setAttribute("data-units", "c");
    } else {
        celsiusButton.className = "btn btn-toggle-unselected";
        fahrenheitButton.className = "btn btn-toggle-selected";
        temperatureGraphContainer.setAttribute("data-units", "f");
    }
    renderTemperatureGraphs();
    renderPressureGraph();

}

function renderTemperatureGraphs() {
    const isCelsius = temperatureGraphContainer.dataset.units === 'c';
    const temperatureUnit = isCelsius ? '°C' : '°F';

    const convertedMonthResults = isCelsius ? tempMonthResults : tempMonthResults.map(convertCelsiusToFahrenheit);
    createGraph(convertedMonthResults,"temp-graph-month", `Temperature (${temperatureUnit})`,
        GraphType.MONTH, monthLabels);

    const convertedWeeklyResults = isCelsius ? tempWeeklyResults : tempWeeklyResults.map(convertCelsiusToFahrenheit);
    createGraph(convertedWeeklyResults,"temp-graph-week", `Temperature (${temperatureUnit})`,
        GraphType.WEEK, weekLabels);

    const convertedDayResults = isCelsius ? tempDayResults : tempDayResults.map(convertCelsiusToFahrenheit);
    createGraph(convertedDayResults,"temp-graph-day", `Temperature (${temperatureUnit})`,
        GraphType.DAY, dayLabels);
}

const renderPressureGraph = () => {


    let pressureMonthData;
    try {
        pressureMonthData = JSON.parse(document.getElementById("pressure-graph-month").dataset.results);
        console.log("Pressure data:", pressureMonthData);
        if (!pressureMonthData) {
            console.error("No data found for pressure graph.");
            return;
        }
    } catch (e) {
        console.error("Error parsing JSON data for pressure graph:", e);
        return;
    }

    createGraph(pressureMonthData, "pressure-graph-month", `Pressure (ATM)`, GraphType.MONTH, monthLabels);
}


function convertCelsiusToFahrenheit(celsiusInput) {
    if (celsiusInput === null) return null;
    return celsiusInput * 1.8 + 32;
}


/**
 * Get graph information for a single day graph.
 * Readings each 30 minutes.
 *
 * @param sensorName Name of sensor used, e.g. Temperature
 * @param data Readings from Arduino
 * @param timeLabels Time labels to be on y-axis
 * @returns tuple graph data object and xLabel, yLabels for graph
 */
function getDayGraphInformation(sensorName, data, timeLabels) {

    return [
        {
        labels: timeLabels,
            datasets: [{
            label: `Average ${sensorName} per Half-hour`,
            data: data,
            fill: true,
            borderColor: GRAPH_COLOR,
            tension: 0.1
        }]
    }, "Time (Half-hourly)", sensorName];
}


/**
 * Get graph information for a month graph.
 * Readings each day.
 *
 * @param sensorName Name of sensor used, e.g Temperature
 * @param data Readings from Arduino
 * @param timeLabels Time labels to be on y-axis
 * @returns tuple graph data object and xLabel, yLabels for graph
 */
function getMonthGraphInformation(sensorName, data, timeLabels) {
    return [
        {
            labels: timeLabels,
            datasets: [{
                label: `Average ${sensorName} per Day`,
                data: data,
                fill: true,
                borderColor: GRAPH_COLOR,
                tension: 0.1
            }]
        }, "Time (Day)", sensorName]
}


/**
 * Get graph information for a week graph.
 * Four readings per day (total 28 readings).
 *
 * @param sensorName Name of sensor used, e.g. Temperature
 * @param data Readings from Arduino
 * @param timeLabels Time labels to be on y-axis
 * @returns tuple graph data object and xLabel, yLabels for graph
 */
function getWeekGraphInformation(sensorName, data, timeLabels) {
    const [nightData, morningData, afternoonData, eveningData] = [[], [], [], []];
    for (let i = 0; i < data.length; i++) {
        switch ((i + 1) % 4) {
            case 1:
                nightData.push(data[i]);
                break;
            case 2:
                morningData.push(data[i]);
                break;
            case 3:
                afternoonData.push(data[i]);
                break;
            default:
                eveningData.push(data[i]);
        }
    }

    return [
        {
            labels: timeLabels,
            datasets: [{
                label: `Night (12:00am - 5:59am)`,
                data: nightData,
                borderColor: WEEK_GRAPH_COLORS[0],
                tension: 0.1
            }, {
                label: `Morning (6:00am - 11:59am)`,
                data: morningData,
                borderColor: WEEK_GRAPH_COLORS[1],
                tension: 0.1
            },
            {
                label: `Afternoon (12:00pm - 5:59pm)`,
                data: afternoonData,
                borderColor: WEEK_GRAPH_COLORS[2],
                tension: 0.1
            },
            {
                label: `Evening (6:00pm - 11:59pm)`,
                data: eveningData,
                borderColor: WEEK_GRAPH_COLORS[3],
                tension: 0.1
            }
            ]
        }, "Time (Day)", sensorName]
}

/**
 * Uses data to create a graph which is generated and displayed in given id
 * @param data          data points for the graph
 * @param graphId       the id of the div where the graph goes
 * @param sensorName    Name of sensor e.g Temperature
 * @param graphType     Type of graph: Month, Week, Day
 * @param timeLabels    Time labels for y-axis
 */
function createGraph(data, graphId, sensorName, graphType, timeLabels) {
    if (!data || data.length === 0){
        console.error("No data points given for data:", data);
        return;
    }

    // Get graph information
    switch (graphType) {
        case GraphType.DAY:
            [dataObject, xLabel, yLabel] = getDayGraphInformation(sensorName, data, timeLabels);
            break;
        case GraphType.WEEK:
            [dataObject, xLabel, yLabel] = getWeekGraphInformation(sensorName, data, timeLabels);
            break;
        case GraphType.MONTH:
            [dataObject, xLabel, yLabel] = getMonthGraphInformation(sensorName, data, timeLabels);
            break;
        default:
            [dataObject, xLabel, yLabel] = getDayGraphInformation(sensorName, data, timeLabels);
    }

    var myChart = new Chart(document.getElementById(graphId),
        {
            type: 'line',
            data: dataObject,
            options: {
                responsive: true,
                maintainAspectRatio: false,
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