// Used for sending rest requests to deployed application
const possible_deployments = ['test', 'prod']
const deployment = window.location.pathname.split('/')[1];
const baseUri = deployment !== undefined && possible_deployments.includes(deployment) ?`/${deployment}` : '';
const setRainPopCloseCookie = () => {
    // console.log(baseUri);
    fetch(`${baseUri}/cookies/set-rains-popup`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        });
}