/**
 * Generator and manager for Graphs for all sensors in garden monitoring page.
 */

// Used for sending rest requests to deployed application
const possible_deployments = ['test', 'prod'];
const deployment = window.location.pathname.split('/')[1];
const baseUri = deployment !== undefined && possible_deployments.includes(deployment) ?`/${deployment}` : '';
const gardenId = deployment !== undefined && possible_deployments.includes(deployment)
    ? window.location.pathname.split('/')[3]
    : window.location.pathname.split('/')[2];

const fahrenheitButton = document.getElementById("fahrenheit-btn");
const celsiusButton = document.getElementById("celsius-btn");

// Containers
const temperatureGraphContainer = document.getElementById("graphs");

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
 * Fetch data from server to get sensor data by period and data type.
 * @param period terms of the data to fetch e.g. month, week, day
 * @param dataType types of data such as temperature, pressure, humidity
 * @returns {Promise<any>} data fetched from server
 */
const fetchData = (period, dataType) => {
    return fetch(`${baseUri}/sensor-data/${period}/${gardenId}/${dataType}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(json => {
            return json.data;
        });
}

// Data variable declarations
let tempMonthResults = undefined;
let tempWeeklyResults = undefined;
let tempDayResults = undefined;
let atmosphereMonthResults = undefined;
let atmosphereWeeklyResults = undefined;
let atmosphereDayResults = undefined;
let moistureMonthResults = undefined;
let moistureWeeklyResults = undefined;
let moistureDayResults = undefined;
let lightMonthResults = undefined;
let lightWeeklyResults = undefined;
let lightDayResults = undefined;
let humidityMonthResults = undefined;
let humidityWeeklyResults = undefined;
let humidityDayResults = undefined;

/**
 * Render all graphs on page load.
 * Manage what graphs are shown / hidden.
 */
window.onload = async function() {
    // Data initialization
    tempMonthResults = await fetchData("month", "temperature");
    tempWeeklyResults = await fetchData("week", "temperature");
    tempDayResults = await fetchData("day", "temperature");
    atmosphereMonthResults = await fetchData("month", "atmosphere");
    atmosphereWeeklyResults = await fetchData("week", "atmosphere");
    atmosphereDayResults = await fetchData("day", "atmosphere");
    moistureMonthResults = await fetchData("month", "moisture");
    moistureWeeklyResults = await fetchData("week", "moisture");
    moistureDayResults = await fetchData("day", "moisture");
    lightMonthResults = await fetchData("month", "light");
    lightWeeklyResults = await fetchData("week", "light");
    lightDayResults = await fetchData("day", "light");
    humidityMonthResults = await fetchData("month", "humidity");
    humidityWeeklyResults = await fetchData("week", "humidity");
    humidityDayResults = await fetchData("day", "humidity");

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

    document.getElementById("month-title").innerHTML = "Temperature Last 30 Days";
    document.getElementById("week-title").innerHTML = "Temperature Last 7 Days";
    document.getElementById("day-title").innerHTML = "Temperature Today";

    const isCelsius = temperatureGraphContainer.dataset.units === 'c';
    const temperatureUnit = isCelsius ? '°C' : '°F';

    const convertedMonthResults = isCelsius ? tempMonthResults : tempMonthResults.map(convertCelsiusToFahrenheit);
    createGraph(convertedMonthResults,"graph-month", `Temperature (${temperatureUnit})`,
        GraphType.MONTH, monthLabels);

    const convertedWeeklyResults = isCelsius ? tempWeeklyResults : tempWeeklyResults.map(convertCelsiusToFahrenheit);
    createGraph(convertedWeeklyResults,"graph-week", `Temperature (${temperatureUnit})`,
        GraphType.WEEK, weekLabels);

    const convertedDayResults = isCelsius ? tempDayResults : tempDayResults.map(convertCelsiusToFahrenheit);
    createGraph(convertedDayResults,"graph-day", `Temperature (${temperatureUnit})`,
        GraphType.DAY, dayLabels);
}

const renderPressureGraph = () => {
    document.getElementById("month-title").innerHTML = "Pressure Last 30 Days";
    document.getElementById("week-title").innerHTML = "Pressure Last 7 Days";
    document.getElementById("day-title").innerHTML = "Pressure Today";
    createGraph(atmosphereMonthResults, "graph-month", `Pressure (ATM)`, GraphType.MONTH, monthLabels);
    createGraph(atmosphereWeeklyResults, "graph-week", `Pressure (ATM)`, GraphType.WEEK, weekLabels);
    createGraph(atmosphereDayResults, "graph-day", `Pressure (ATM)`, GraphType.DAY, dayLabels);
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