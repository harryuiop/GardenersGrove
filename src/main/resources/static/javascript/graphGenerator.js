/**
 * Generator and manager for Graphs for all sensors in garden monitoring page.
 */

const tempUnits = document.getElementById('temp-units');
const fahrenheitButton = document.getElementById("fahrenheit-btn");
const celsiusButton = document.getElementById("celsius-btn");
const currentTempUnit = document.getElementById("current-temp-unit");
const currentTempReading = document.getElementById("current-temp-reading");

// Containers
const temperatureGraphContainer = document.getElementById("graphs");
const disconnectedWarning = document.getElementById("disconnected-warning");

// Labels
const graphDataSet = document.getElementById("display-graphs").dataset;
const monthLabels = JSON.parse(graphDataSet.monthLabels);
const dayLabels = JSON.parse(graphDataSet.dayLabels);
const weekLabels = JSON.parse(graphDataSet.weekLabels);

const GRAPH_COLOR = 'rgb(75, 192, 192)';
const WEEK_GRAPH_COLORS = ['rgb(44, 62, 80)', 'rgb(241, 196, 15)', 'rgb(52, 152, 219)', 'rgb(231, 76, 60)'];

// Graph Type enum, used for labelling.
const GraphType = Object.freeze({
    MONTH: 0, WEEK: 1, DAY: 2
});

// graph declarations
let monthGraph, weekGraph, dayGraph;
let currentlySelectedSensorView = "Temperature";

/**
 * Render temperature graphs on page load.
 */
window.onload = function() {
    renderTemperatureGraphs();
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
    })

    currentlySelectedSensorView = buttonId;

    switch (buttonId){
        case "Temperature":
            renderTemperatureGraphs();
            break;
        case "Moisture":
            renderMoistureGraph();
            break;
        case "Light":
            renderLightGraph();
            break;
        case "Pressure":
            renderPressureGraph();
            break;
        case "Humidity":
            renderHumidityGraph();
            break;
        default:
            console.error("Graph button id is invalid");
    }
}

/**
 * Change temperature unit to Fahrenheit or Celsius, update graph, update current temperature
 * @param {string} unit c for Celsius, anything else for Fahrenheit
 */
function changeTemperatureUnit(unit) {
    const currentTempUnitText = currentTempUnit.innerText;

    if (unit === 'c') {
        celsiusButton.className = "btn btn-toggle-selected";
        fahrenheitButton.className = "btn btn-toggle-unselected";
        temperatureGraphContainer.setAttribute("data-units", "c");

        // Change in unit
        if (currentTempUnitText === "°F" && !isNaN(Number(currentTempReading.innerText))) {
            currentTempReading.innerText = convertFahrenheitToCelsius(parseFloat(currentTempReading.innerText)).toFixed(1);
        }
        currentTempUnit.innerText = "°C";

    } else {
        celsiusButton.className = "btn btn-toggle-unselected";
        fahrenheitButton.className = "btn btn-toggle-selected";
        temperatureGraphContainer.setAttribute("data-units", "f");

        // Change in unit
        if (currentTempUnitText === "°C" && !isNaN(Number(currentTempReading.innerText))) {
            currentTempReading.innerText = convertCelsiusToFahrenheit(parseFloat(currentTempReading.innerText)).toFixed(1);
        }
        currentTempUnit.innerText = "°F";

    }
    renderTemperatureGraphs();
}

const changeGraphTitle  = (monthTitle, weekTitle, dayTitle) => {
    document.getElementById("month-title").innerHTML = monthTitle;
    document.getElementById("week-title").innerHTML = weekTitle;
    document.getElementById("day-title").innerHTML = dayTitle;
}

const destroyGraphs = () => {
    if(monthGraph) {
        monthGraph.destroy();
        weekGraph.destroy();
        dayGraph.destroy();
    }
}

/**
 * Destroys all graphs and render temperature graphs.
 */
function renderTemperatureGraphs() {
    const tempMonthResults = JSON.parse(graphDataSet.monthTemp);
    const tempWeeklyResults = JSON.parse(graphDataSet.weekTemp);
    const tempDayResults = JSON.parse(graphDataSet.dayTemp);

    tempUnits.style.display = "block";

    changeGraphTitle("Temperature Last 30 Days", "Temperature Last 7 Days", "Temperature Today");

    // reset graphs
    destroyGraphs();

    const isCelsius = temperatureGraphContainer.dataset.units === 'c';
    const temperatureUnit = isCelsius ? '°C' : '°F';

    const convertedMonthResults = isCelsius ? tempMonthResults : tempMonthResults.map(convertCelsiusToFahrenheit);
    monthGraph = createGraph(getMonthGraphInformation(`Temperature (${temperatureUnit})`, convertedMonthResults), "graph-month")

    const convertedWeeklyResults = isCelsius ? tempWeeklyResults : tempWeeklyResults.map(convertCelsiusToFahrenheit);
    weekGraph = createGraph(getWeekGraphInformation(`Temperature (${temperatureUnit})`, convertedWeeklyResults),"graph-week")

    const convertedDayResults = isCelsius ? tempDayResults : tempDayResults.map(convertCelsiusToFahrenheit);
    dayGraph = createGraph(getDayGraphInformation(`Temperature (${temperatureUnit})`, convertedDayResults), "graph-day")

    alertMessage("Temperature")
}

/**
 * Destroys all graphs and render Moisture graphs.
 */
const renderMoistureGraph = () => {
    const moistureMonthResults = JSON.parse(graphDataSet.monthMoisture);
    const moistureWeeklyResults = JSON.parse(graphDataSet.weekMoisture);
    const moistureDayResults = JSON.parse(graphDataSet.dayMoisture);

    tempUnits.style.display = "none";
    changeGraphTitle("Soil Moisture Last 30 Days", "Soil Moisture Last 7 Days", "Soil Moisture Today");

    // reset graphs
    destroyGraphs();

    monthGraph = createGraph(getMonthGraphInformation("Soil Moisture", moistureMonthResults), "graph-month");
    weekGraph = createGraph(getWeekGraphInformation("Soil Moisture", moistureWeeklyResults), "graph-week");
    dayGraph = createGraph(getDayGraphInformation("Soil Moisture", moistureDayResults), "graph-day");

    alertMessage("Moisture")
}

/**
 * Destroys all graphs and render light graphs.
 */
const renderLightGraph = () => {
    const lightMonthResults = JSON.parse(graphDataSet.monthLight);
    const lightWeeklyResults = JSON.parse(graphDataSet.weekLight);
    const lightDayResults = JSON.parse(graphDataSet.dayLight);

    tempUnits.style.display = "none";

    changeGraphTitle("Light Level Last 30 Days", "Light Level Last 7 Days", "Light Level Today");

    // reset graphs
    destroyGraphs();

    monthGraph = createGraph(getMonthGraphInformation("Light", lightMonthResults), "graph-month");
    weekGraph = createGraph(getWeekGraphInformation("Light", lightWeeklyResults), "graph-week");
    dayGraph = createGraph(getDayGraphInformation("Light", lightDayResults), "graph-day");

    alertMessage("Light")
}

/**
 * Destroys all graphs and render pressure graphs.
 */
const renderPressureGraph = () => {

    const pressureMonthResults = JSON.parse(graphDataSet.monthPressure);
    const pressureWeeklyResults = JSON.parse(graphDataSet.weekPressure);
    const pressureDayResults = JSON.parse(graphDataSet.dayPressure);

    tempUnits.style.display = "none";

    changeGraphTitle("Pressure Last 30 Days", "Pressure Last 7 Days", "Pressure Today");``

    // reset graphs
    destroyGraphs();

    monthGraph = createGraph(getMonthGraphInformation(`Pressure (ATM)`, pressureMonthResults), "graph-month");
    weekGraph = createGraph(getWeekGraphInformation(`Pressure (ATM)`, pressureWeeklyResults), "graph-week");
    dayGraph = createGraph(getDayGraphInformation(`Pressure (ATM)`, pressureDayResults), "graph-day");

    alertMessage("Air-Pressure")
}

/**
 * Destroys all graphs and render humidity graphs.
 */
const renderHumidityGraph = () => {
    const humidityMonthResults = JSON.parse(graphDataSet.monthHumidity);
    const humidityWeeklyResults = JSON.parse(graphDataSet.weekHumidity);
    const humidityDayResults = JSON.parse(graphDataSet.dayHumidity);

    tempUnits.style.display = "none";

    changeGraphTitle("Humidity Last 30 Days", "Humidity Last 7 Days", "Humidity Today");

    // reset graphs
    destroyGraphs();

    monthGraph = createGraph(getMonthGraphInformation("Humidity", humidityMonthResults), "graph-month");
    weekGraph = createGraph(getWeekGraphInformation("Humidity", humidityWeeklyResults), "graph-week");
    dayGraph = createGraph(getDayGraphInformation("Humidity", humidityDayResults), "graph-day");

    alertMessage("Humidity")
}


function convertCelsiusToFahrenheit(celsiusInput) {
    if (celsiusInput === null) return null;
    return celsiusInput * 1.8 + 32;
}

function convertFahrenheitToCelsius(fahrenheit) {
    return (fahrenheit - 32) / 1.8;

}

/**
 * Get graph information for a single day graph.
 * Readings each 30 minutes.
 *
 * @param sensorName Name of sensor used, e.g. Temperature
 * @param data Readings from Arduino
 * @returns tuple graph data object and xLabel, yLabels for graph
 */
function getDayGraphInformation(sensorName, data) {

    return [
        {
        labels: dayLabels,
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
 * @param sensorName Name of sensor used, e.g. Temperature
 * @param data Readings from Arduino
 * @returns tuple graph data object and xLabel, yLabels for graph
 */
function getMonthGraphInformation(sensorName, data) {
    return [
        {
            labels: monthLabels,
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
 * @returns tuple graph data object and xLabel, yLabels for graph
 */
function getWeekGraphInformation(sensorName, data) {
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
            labels: weekLabels,
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
