/**
 * Generator and manage Graphs for all sensors in garden monitoring page
 */

const fahrenheitButton = document.getElementById("fahrenheit-btn");
const celsiusButton = document.getElementById("celsius-btn");
const temperatureGraphContainer = document.getElementById("temperature-graphs");
const tempMonthResults = JSON.parse(document.getElementById("temp-graph-month").dataset.results);
const tempWeeklyResults = JSON.parse(document.getElementById("temp-graph-week").dataset.results);
const tempDayResults = JSON.parse(document.getElementById("temp-graph-day").dataset.results);

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
    createGraph(convertedMonthResults,"temp-graph-month", "Average Daily Temp Over last 30 days", "Days", "Average Temp" );

    const convertedWeeklyResults = isCelsius ? tempWeeklyResults : tempWeeklyResults.map(convertCelsiusToFahrenheit);
    createGraph(convertedWeeklyResults,"temp-graph-week", "Average Daily Temp Over last 7 days", "Days", "Average Temp" );

    const convertedDayResults = isCelsius ? tempDayResults : tempDayResults.map(convertCelsiusToFahrenheit);
    createGraph(convertedDayResults,"temp-graph-day", "Average Daily Temp Over last day", "Time (each 30 mins)", "Average Temp" );
}

function convertCelsiusToFahrenheit(celsiusInput) {
    if (celsiusInput === null) return null;
    return celsiusInput * 1.8 + 32;
}

/**
 * @param count Number of labels.
 * @returns X axis labels corresponding to time.
 */
function labelLength(count) {
    return Array.from({length: count}, (_, index) => index + 1);
}

/**
 * Uses data to create a graph which is generated and displayed in given id
 * @param data          data points for the graph
 * @param graphId       the id of the div where the graph goes
 * @param title         the title of the graph
 * @param xLabel        x label name
 * @param yLabel        y label name
 */
function createGraph(data, graphId, title, xLabel, yLabel) {
    if (!data || data.length === 0){
        console.log(data)
        console.log("No data points given")
        return;
    }

    const labels = labelLength(8)

    new Chart(
        document.getElementById(graphId),
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