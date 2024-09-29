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

// graph declarations
let monthGraph, weekGraph, dayGraph;
let currentlySelectedSensorView = "Temperature";

/**
 * Render temperature graphs on page load.
 */
window.onload = () => renderTemperatureGraphs();

/**
 * Make graphs for that sensor shown.
 * @param {string} buttonId Sensor selected
 */
function makeActive(buttonId) {
    const allButtons = ['Temperature', 'Moisture', 'Light', 'Pressure', 'Humidity']
    const button = document.getElementById(buttonId);

    button.classList.add('btn-stats-bar-active');

    allButtons.forEach(allButtonsItem => {
        if (allButtonsItem !== buttonId) {
            const otherButton = document.getElementById(allButtonsItem);
            otherButton.classList.add('btn-stats-bar');
            otherButton.classList.remove('btn-stats-bar-active');
        }
    });

    currentlySelectedSensorView = buttonId;

    switch (buttonId) {
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

function changeGraphTitle(monthTitle, weekTitle, dayTitle) {
    document.getElementById("month-title").innerHTML = monthTitle;
    document.getElementById("week-title").innerHTML = weekTitle;
    document.getElementById("day-title").innerHTML = dayTitle;
}

function destroyGraphs() {
    if (monthGraph) {
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
    const minTemperature = JSON.parse(graphDataSet.minTemp);
    const maxTemperature = JSON.parse(graphDataSet.maxTemp);

    tempUnits.style.display = "block";

    changeGraphTitle("Temperature Last 30 Days", "Temperature Last 7 Days", "Temperature Today");

    // reset graphs
    destroyGraphs();

    const isCelsius = temperatureGraphContainer.dataset.units === 'c';
    const temperatureUnit = isCelsius ? '°C' : '°F';

    const convertedMonthResults = isCelsius ? tempMonthResults : tempMonthResults.map(convertCelsiusToFahrenheit);
    monthGraph = createGraph(getMonthGraphInformation(`Temperature (${temperatureUnit})`, convertedMonthResults), "graph-month", minTemperature, maxTemperature)

    const convertedWeeklyResults = isCelsius ? tempWeeklyResults : tempWeeklyResults.map(convertCelsiusToFahrenheit);
    weekGraph = createGraph(getWeekGraphInformation(`Temperature (${temperatureUnit})`, convertedWeeklyResults), "graph-week", minTemperature, maxTemperature)

    const convertedDayResults = isCelsius ? tempDayResults : tempDayResults.map(convertCelsiusToFahrenheit);
    dayGraph = createGraph(getDayGraphInformation(`Temperature (${temperatureUnit})`, convertedDayResults), "graph-day", minTemperature, maxTemperature)

    alertMessage("Temperature")
}

/**
 * Destroys all graphs and render Moisture graphs.
 */
function renderMoistureGraph() {
    const moistureMonthResults = JSON.parse(graphDataSet.monthMoisture);
    const moistureWeeklyResults = JSON.parse(graphDataSet.weekMoisture);
    const moistureDayResults = JSON.parse(graphDataSet.dayMoisture);
    const minMoisture = JSON.parse(graphDataSet.minMoisture);
    const maxMoisture = JSON.parse(graphDataSet.maxMoisture);

    tempUnits.style.display = "none";
    changeGraphTitle("Soil Moisture Last 30 Days", "Soil Moisture Last 7 Days", "Soil Moisture Today");

    // reset graphs
    destroyGraphs();

    monthGraph = createGraph(getMonthGraphInformation("Soil Moisture", moistureMonthResults), "graph-month", minMoisture, maxMoisture);
    weekGraph = createGraph(getWeekGraphInformation("Soil Moisture", moistureWeeklyResults), "graph-week", minMoisture, maxMoisture);
    dayGraph = createGraph(getDayGraphInformation("Soil Moisture", moistureDayResults), "graph-day", minMoisture, maxMoisture);

    alertMessage("Moisture")
}

/**
 * Destroys all graphs and render light graphs.
 */
function renderLightGraph() {
    const lightMonthResults = JSON.parse(graphDataSet.monthLight);
    const lightWeeklyResults = JSON.parse(graphDataSet.weekLight);
    const lightDayResults = JSON.parse(graphDataSet.dayLight);

    tempUnits.style.display = "none";

    changeGraphTitle("Light Level Last 30 Days", "Light Level Last 7 Days", "Light Level Today");

    // reset graphs
    destroyGraphs();

    monthGraph = createGraph(getMonthGraphInformation("Light", lightMonthResults), "graph-month", 0, 100);
    weekGraph = createGraph(getWeekGraphInformation("Light", lightWeeklyResults), "graph-week", 0, 100);
    dayGraph = createGraph(getDayGraphInformation("Light", lightDayResults), "graph-day", 0, 100);

    alertMessage("Light")
}

/**
 * Destroys all graphs and render pressure graphs.
 */
function renderPressureGraph() {
    const pressureMonthResults = JSON.parse(graphDataSet.monthPressure);
    const pressureWeeklyResults = JSON.parse(graphDataSet.weekPressure);
    const pressureDayResults = JSON.parse(graphDataSet.dayPressure);
    const minPressure = JSON.parse(graphDataSet.minPressure);
    const maxPressure = JSON.parse(graphDataSet.maxPressure);

    tempUnits.style.display = "none";

    changeGraphTitle("Pressure Last 30 Days", "Pressure Last 7 Days", "Pressure Today");

    // reset graphs
    destroyGraphs();

    monthGraph = createGraph(getMonthGraphInformation(`Pressure (ATM)`, pressureMonthResults), "graph-month", minPressure, maxPressure);
    weekGraph = createGraph(getWeekGraphInformation(`Pressure (ATM)`, pressureWeeklyResults), "graph-week", minPressure, maxPressure);
    dayGraph = createGraph(getDayGraphInformation(`Pressure (ATM)`, pressureDayResults), "graph-day", minPressure, maxPressure);

    alertMessage("Air-Pressure")
}

/**
 * Destroys all graphs and render humidity graphs.
 */
function renderHumidityGraph() {
    const humidityMonthResults = JSON.parse(graphDataSet.monthHumidity);
    const humidityWeeklyResults = JSON.parse(graphDataSet.weekHumidity);
    const humidityDayResults = JSON.parse(graphDataSet.dayHumidity);
    const minHumidity = JSON.parse(graphDataSet.monthHumidity);
    const maxHumidity = JSON.parse(graphDataSet.maxHumidity);
    tempUnits.style.display = "none";

    changeGraphTitle("Humidity Last 30 Days", "Humidity Last 7 Days", "Humidity Today");

    // reset graphs
    destroyGraphs();

    monthGraph = createGraph(getMonthGraphInformation("Humidity", humidityMonthResults), "graph-month", minHumidity, maxHumidity);
    weekGraph = createGraph(getWeekGraphInformation("Humidity", humidityWeeklyResults), "graph-week", minHumidity, maxHumidity);
    dayGraph = createGraph(getDayGraphInformation("Humidity", humidityDayResults), "graph-day", minHumidity, maxHumidity);

    alertMessage("Humidity")
}


/**
 * Converts a temperature in degrees celsius to degrees fahrenheit.
 * @param celsiusInput The temperature in degrees celsius.
 * @returns {number|null} The temperature in degrees fahrenheit.
 */
function convertCelsiusToFahrenheit(celsiusInput) {
    if (celsiusInput === null) return null;
    return celsiusInput * 1.8 + 32;
}

/**
 * Converts a temperature in degrees Fahrenheit to degrees Celsius.
 * @param fahrenheit a temperature in degrees Fahrenheit.
 * @returns {number} the temperature in degrees Celsius.
 */
function convertFahrenheitToCelsius(fahrenheit) {
    return (fahrenheit - 32) / 1.8;

}

/**
 * Get graph information for a single-day graph.
 * Readings each 30 minutes.
 *
 * @param {string} sensorName Name of sensor used, e.g., Temperature
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
 * @param sensorName Name of sensor used, e.g., Temperature
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
            }, {
                label: `Afternoon (12:00pm - 5:59pm)`,
                data: afternoonData,
                borderColor: WEEK_GRAPH_COLORS[2],
                tension: 0.1
            }, {
                label: `Evening (6:00pm - 11:59pm)`,
                data: eveningData,
                borderColor: WEEK_GRAPH_COLORS[3],
                tension: 0.1
            }]
        }, "Time (Day)", sensorName]
}

/**
 * Draws shading rectangles onto the charts to show advice ranges
 * @param {number} minValue The advice range lower bound
 * @param {number} maxValue The advice range upper bound
 */
function updateRectangleConfig(minValue, maxValue) {
    Chart.plugins.register({
        beforeDraw: function (chart) {
            if (chart.config.options.shadedRegion) {
                const ctx = chart.chart.ctx;
                const y = chart.scales['y-axis-0'];

                ctx.reset();
                ctx.save();
                ctx.fillStyle = 'rgba(150, 0, 0, 0.1)';

                const xStart = chart.chartArea.left;
                const xEnd = chart.chartArea.right;

                // upper limit shade
                const yStart1 = chart.chartArea.top;
                let yEnd1 = y.getPixelForValue(maxValue);

                // Only draw shading if height of rectangle is positive (the range is visible on the drawn graph)
                if (yEnd1 - yStart1 > 0) {
                    // Prevent the shade rectangle from drawing over the x-axis labels
                    yEnd1 = Math.min(yEnd1, chart.chartArea.bottom);
                    ctx.fillRect(xStart, yStart1, xEnd - xStart, yEnd1 - yStart1);
                }

                // lower limit shade
                let yStart2 = y.getPixelForValue(minValue);
                const yEnd2 = chart.chartArea.bottom;

                if (yEnd2 - yStart2 > 0) {
                    // Prevent the shade rectangle from drawing over the graph legend
                    if (yStart2 < 0) {
                        yStart2 = chart.chartArea.top;
                    }
                    ctx.fillRect(xStart, yStart2, xEnd - xStart, yEnd2 - yStart2);
                }

                ctx.restore();
            }
        }
    });
}

/**
 * Uses data to create a graph which is generated and displayed in given id
 * @param data          data points for the graph
 * @param graphId       the id of the div where the graph goes
 * @param minValue      The lower bound of the advice range
 * @param maxValue      The upper bound of the advice range
 */
function createGraph([dataObject, xLabel, yLabel], graphId, minValue, maxValue) {
    updateRectangleConfig(minValue, maxValue);
    return new Chart(document.getElementById(graphId),
        {
            type: 'line',
            data: dataObject,
            options: {
                responsive: true,
                maintainAspectRatio: true,
                aspectRatio: 1.25,
                scales: {
                    yAxes: {
                        scaleLabel: {
                            display: true,
                            labelString: yLabel
                        }
                    },
                    xAxes: {
                        scaleLabel: {
                            display: true,
                            labelString: xLabel
                        }
                    }
                },
                shadedRegion: true
            }
        }
    )
}