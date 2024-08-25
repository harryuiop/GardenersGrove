/**
 * Generator and manager for Graphs for all sensors in garden monitoring page.
 */

const fahrenheitButton = document.getElementById("fahrenheit-btn");
const celsiusButton = document.getElementById("celsius-btn");
const temperatureGraphContainer = document.getElementById("temperature-graphs");
const tempMonthResults = JSON.parse(document.getElementById("temp-graph-month").dataset.results);
// const tempWeeklyResults = JSON.parse(document.getElementById("temp-graph-week").dataset.results);
const tempDayResults = JSON.parse(document.getElementById("temp-graph-day").dataset.results);


console.log(document.getElementById("temp-graph-month").dataset.labels);
const monthLabels = JSON.parse(document.getElementById("temp-graph-month").dataset.labels);
const dayLabels = JSON.parse(document.getElementById("temp-graph-day").dataset.labels);


const GRAPH_COLOR = 'rgb(75, 192, 192)';
const WEEK_GRAPH_COLORS = ['rgb(44, 62, 80)', 'rgb(241, 196, 15)', 'rgb(52, 152, 219)', 'rgb(231, 76, 60)'];

const DAYS_IN_WEEK = 7;
const MONTH_STRINGS = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
const DAY_STRINGS = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

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

}

function renderTemperatureGraphs() {
    const isCelsius = temperatureGraphContainer.dataset.units === 'c';
    const temperatureUnit = isCelsius ? '°C' : '°F';

    const convertedMonthResults = isCelsius ? tempMonthResults : tempMonthResults.map(convertCelsiusToFahrenheit);
    createGraph(convertedMonthResults,"temp-graph-month", `Temperature (${temperatureUnit})`,
        GraphType.MONTH, monthLabels);

    // const convertedWeeklyResults = isCelsius ? tempWeeklyResults : tempWeeklyResults.map(convertCelsiusToFahrenheit);
    // createGraph(convertedWeeklyResults,"temp-graph-week", `Temperature (${temperatureUnit})`, GraphType.WEEK);

    const convertedDayResults = isCelsius ? tempDayResults : tempDayResults.map(convertCelsiusToFahrenheit);
    createGraph(convertedDayResults,"temp-graph-day", `Temperature (${temperatureUnit})`,
        GraphType.DAY, dayLabels);
}


function convertCelsiusToFahrenheit(celsiusInput) {
    if (celsiusInput === null) return null;
    return celsiusInput * 1.8 + 32;
}


/**
 * Get graph information for a single day graph.
 * Readings each 30 minutes.
 *
 * @param sensorName Name of sensor used, e.g Temperature
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
 * @param sensorName Name of sensor used, e.g Temperature
 * @param data Readings from Arduino
 * @returns tuple graph data object and xLabel, yLabels for graph
 */
function getWeekGraphInformation(sensorName, data) {
    const timeLabels = [];
    const currentDay = new Date();
    currentDay.setDate(currentDay.getDate() - DAYS_IN_WEEK - 1);
    for (let i = 1; i < DAYS_IN_WEEK + 1; i++) {
        const newDay = new Date(currentDay);
        newDay.setDate(currentDay.getDate() + i);
        timeLabels.push(`${DAY_STRINGS[newDay.getDay()]}, ${newDay.getDate()} ${MONTH_STRINGS[newDay.getMonth()]}`);
    }
    return [
        {
            labels: timeLabels,
            datasets: [{
                label: `Night (12:00am - 5:59am)`,
                data: data.slice(0, 7),
                borderColor: WEEK_GRAPH_COLORS[0],
                tension: 0.1
            }, {
                label: `Morning (6:00am - 11:59am)`,
                data: data.slice(7, 14),
                borderColor: WEEK_GRAPH_COLORS[1],
                tension: 0.1
            },
            {
                label: `Afternoon (12:00pm - 5:59pm)`,
                data: data.slice(14, 21),
                borderColor: WEEK_GRAPH_COLORS[2],
                tension: 0.1
            },
            {
                label: `Evening (6:00pm - 11:59pm)`,
                data: data.slice(21, 28),
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
        console.log(data)
        console.error("No data points given")
        return;
    }

    // Get graph information
    let [dataObject, xLabel, yLabel] = [{}, '', ''];
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
            [dataObject, xLabel, yLabel] = getDayGraphInformation(sensorName, data);
    }

    console.log(dataObject);

    var myChart = new Chart(document.getElementById(graphId),
        {
            type: 'line',
            data: dataObject,
            options: {
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