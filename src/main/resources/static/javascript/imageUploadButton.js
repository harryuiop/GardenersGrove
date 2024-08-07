document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('file-button').addEventListener(
        'click', () => document.getElementById('file-picker').click()
    );

    document.getElementById('file-picker').addEventListener(
        'change', () => document.getElementById('submit-button').click()
    );
});
