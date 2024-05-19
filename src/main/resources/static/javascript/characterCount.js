/**
 * Updates the character counter with every change of input to the garden description field
 */
(() => {
    const counter = (() => {
        const input = document.getElementById('gardenDescription'),
            display = document.getElementById('counter-display'),
            changeEvent = (evt) => display.innerHTML = (evt.target.value.length),
            getInput = () => input.value,
            countEvent = () => input.addEventListener('keyup', changeEvent),
            init = () => countEvent();
        loadEvent();

        return {
            init: init
        }

    })();

    counter.init();

})();