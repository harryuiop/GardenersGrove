window.onload = function() {
    const monthResults = document.getElementById("temp-graph").dataset.results;
    console.log(monthResults)
    addData(JSON.parse(monthResults),"temp-graph", "Average Daily Temp Over last 30 days", "Days", "Average Temp" );
    const weeklyResults = document.getElementById("temp-graph2").dataset.results;
    addData(JSON.parse(weeklyResults),"temp-graph2", "Average Daily Temp Over last 7 days", "Days", "Average Temp" );
}
function labelLength(count) {
    return Array.from({ length: count }, (_, index)=>index+1);

}

/**
 * Uses data to create a graph which is generated and displayed in given id
 * @param data          data points for the graph
 * @param graphId       the id of the div where the graph goes
 * @param title         the title of the graph
 * @param xAxisLabel    x label name
 * @param yAxisLabel    y label name
 */
function addData(data, graphId, title, xAxisLabel, yAxisLabel) {
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
                            labelString: yAxisLabel
                        }
                    }],
                    xAxes: [{
                        scaleLabel: {
                            display: true,
                            labelString: xAxisLabel
                        }
                    }]
                }
            }
        }
    )
}