
const setRainPopCloseCookie = () => {
    const baseURL = window.location.origin;
    fetch(`https://${baseURL}/cookies/set-rains-popup`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        });
}