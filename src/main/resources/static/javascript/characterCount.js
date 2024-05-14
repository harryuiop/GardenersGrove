(() => {
    const counter = (() => {
        const input = document.getElementById('gardenDescription'),
            display = document.getElementById('counter-display'),
            changeEvent = (evt) => display.innerHTML = (evt.target.value.length),
            getInput = () => input.value,
            countEvent = () => input.addEventListener('keyup', changeEvent),
            loadEvent = () => document.addEventListener('onload', changeEvent),
            init = () => countEvent();
        loadEvent();

        return {
            init: init
        }

    })();

    counter.init();

})();