/**
 * Generator and manage Graphs for all sensors in garden monitoring page
 */

const fahrenheitButton = document.getElementById("fahrenheit-btn");
const celsiusButton = document.getElementById("celsius-btn");
const temperatureGraphContainer = document.getElementById("temperature-graphs");
const tempMonthResults = JSON.parse(document.getElementById("temp-graph-month").dataset.results);
const tempWeeklyResults = JSON.parse(document.getElementById("temp-graph-week").dataset.results);
const tempDayResults = JSON.parse(document.getElementById("temp-graph-day").dataset.results);

const HOURS_IN_DAY = 24;
const DAYS_IN_MONTH = 30;
const monthStrings = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

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
        renderTemperatureGraphs();
    } else {
        celsiusButton.className = "btn btn-toggle-unselected";
        fahrenheitButton.className = "btn btn-toggle-selected";
        temperatureGraphContainer.setAttribute("data-units", "f");
        renderTemperatureGraphs();
    }
}

function renderTemperatureGraphs() {
    console.log(temperatureGraphContainer.dataset);
    const isCelsius = temperatureGraphContainer.dataset.units === 'c';

    const convertedMonthResults = isCelsius ? tempMonthResults : tempMonthResults.map(convertCelsiusToFahrenheit);
    createGraph(convertedMonthResults,"temp-graph-month", "Temperature", GraphType.MONTH);

    const convertedWeeklyResults = isCelsius ? tempWeeklyResults : tempWeeklyResults.map(convertCelsiusToFahrenheit);
    createGraph(convertedWeeklyResults,"temp-graph-week", "Temperature", GraphType.WEEK);

    const convertedDayResults = isCelsius ? tempDayResults : tempDayResults.map(convertCelsiusToFahrenheit);
    createGraph(convertedDayResults,"temp-graph-day", "Temperature", GraphType.DAY);
}

function convertCelsiusToFahrenheit(celsiusInput) {
    if (celsiusInput === null) return null;
    return celsiusInput * 1.8 + 32;
}


/**
 * Get graph information for a time graph.
 *
 * @param sensorName Name of sensor used, e.g Temperature
 * @returns tuple of labels, graph title, xLabel, yLabel
 */
function getTimeGraphInformation(sensorName) {
    let timeLabels = [];
    for (let hour = 0; hour < HOURS_IN_DAY; hour++) {
        timeLabels.push(`${hour}: 00`);
        timeLabels.push(`${hour}: 30`);
    }
    return [timeLabels, `Average ${sensorName} over last day`, "Time of day", sensorName];
}

function getMonthGraphInformation(sensorName) {
    const timeLabels = [];
    const currentDay = new Date();
    currentDay.setDate(currentDay.getDate() - DAYS_IN_MONTH);
    for (let i = 1; i < DAYS_IN_MONTH + 1; i++) {
        const newDay = new Date(currentDay);
        newDay.setDate(currentDay.getDate() + i);
        timeLabels.push(`${newDay.getDate()}  ${monthStrings[newDay.getMonth()]}`);
    }
    return [timeLabels, `Average ${sensorName} over last day`, "Time of day", sensorName];
}

/**
 * Uses data to create a graph which is generated and displayed in given id
 * @param data          data points for the graph
 * @param graphId       the id of the div where the graph goes
 * @param title         the title of the graph
 * @param xLabel        x label name
 * @param yLabel        y label name
 */
function createGraph(data, graphId, sensorName, graphType) {
    if (!data || data.length === 0){
        console.log(data)
        console.log("No data points given")
        return;
    }

    // Get graph information
    // const [labels, title, xLabel, yLabel] = getTimeGraphInformation(sensorName);

    const [labels, title, xLabel, yLabel] = getMonthGraphInformation(sensorName);

    var myChart = new Chart(document.getElementById(graphId),
        {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: title,
                    data: data,
                    fill: true,
                    borderColor: 'rgb(75, 192, 192)',
                    tension: 0.1
                }]
            },
            options: {
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
                }
            }
        }
    )
}