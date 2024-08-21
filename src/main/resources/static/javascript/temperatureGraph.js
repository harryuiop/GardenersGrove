import {Chart} from "chart.js/auto";

const possible_deployments = ['test', 'prod']

function getData() {
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

function labelLength(count) {
    return Array.from({ length: count }, (_, index)=>index+1);

}

async function addData(data) {
    if (!data || data.length === 0){
        console.log("No data points given")
        return;
    }
    const labels = labelLength(30)
    const graphData = {
        labels: labels,
        datasets: [{
            label: "Average Daily Temperature",
            data: data,
            fill: true,
            borderColor: 'rgb(75, 192, 192)',
            tension: 0.1
        }]
    };

    const config = {
        type: 'line',
        data: graphData
    }

    new Chart(
        document.getElementById("temp-graph"),
        {
            config
        }
    )
}