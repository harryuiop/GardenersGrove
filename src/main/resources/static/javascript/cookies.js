
const setRainPopupCookie = () => {

    // Used for sending rest requests to deployed application
    const possible_deployments = ['test', 'prod']
    const deployment = window.location.pathname.split('/')[1];
    const baseUri = deployment !== undefined && possible_deployments.includes(deployment) ?`/${deployment}` : '';

    fetch(`${baseUri}/cookies/set-rain-popup`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });

    document.getElementById("advice-message").hidden = true;
}