function updateCharCount(event) {
    const input = document.getElementById('gardenDescription');
    const display = document.getElementById('counter-display');
    display.innerHTML = input.value.length;
}

