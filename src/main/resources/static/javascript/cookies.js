
const setRainPopupCookie = () => {

    // Used for sending rest requests to deployed application
    const possible_deployments = ['test', 'prod']
    console.log(window.location.pathname);
    const deployment = window.location.pathname.split('/')[1];
    const baseUri = deployment !== undefined && possible_deployments.includes(deployment) ?`/${deployment}` : '';
    const gardenId = deployment !== undefined && possible_deployments.includes(deployment)
                                            ? window.location.pathname.split('/')[3]
                                            : window.location.pathname.split('/')[2];
    console.log(gardenId);
    fetch(`${baseUri}/cookies/set-rain-popup/${gardenId}`)
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