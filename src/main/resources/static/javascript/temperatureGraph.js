window.onload = function() {
    const monthResults = document.getElementById("temp-graph").dataset.results;
    addData(JSON.parse(monthResults));
}
function labelLength(count) {
    return Array.from({ length: count }, (_, index)=>index+1);

}

function addData(data) {
    if (!data || data.length === 0){
        console.log(data)
        console.log("No data points given")
        return;
    }
    const labels = labelLength(8)

    new Chart(
        document.getElementById("temp-graph"),
        {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: "Average Daily Temperature",
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
                            labelString: "Average Temperature"
                        }
                    }],
                    xAxes: [{
                        scaleLabel: {
                            display: true,
                            labelString: "Past days"
                        }
                    }]
                }
            }
        }
    )
}