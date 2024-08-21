const possible_deployments = ['test', 'prod']

function addData(data) {
    var chart = new CanvasJS.Chart("chartContainer", {
        animationEnabled: true,
        title: {
            text: "Hourly Average CPU Utilization"
        },
        axisX: {
            title: "Time"
        },
        axisY: {
            title: "Percentage",
            suffix: "%",
            includeZero: true
        },
        data: [{
            type: "line",
            name: "CPU Utilization",
            connectNullData: true,
            //nullDataLineDashType: "solid",
            xValueType: "dateTime",
            xValueFormatString: "DD MMM hh:mm TT",
            yValueFormatString: "#,##0.##\"%\"",
            dataPoints: data
        }]
    });
    chart.render();
}

window.onload = function() {
    const deployment = window.location.pathname.split('/')[1];
    const baseUri = deployment !== undefined && possible_deployments.includes(deployment) ?`/${deployment}` : '';
    fetch(`${baseUri}/temp-data`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok")
            }
            return response.json();
        })
        .then(data => {
            addData(data);
        })
        .catch(error => {
            console.error("There was a problem fetching temperature data:", error);
        })
}