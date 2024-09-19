/**
 * Emoji checker to look for emojis in the email address entered by the user
 * This prevents the form from being submitted if there is an emoji
 * @type {HTMLElement}
 */

const loginForm = document.getElementById("loginForm");
const usernameInput = document.getElementById("username");
const emailError = document.getElementById("emailError");
const invalidError = document.getElementById("invalidError");

/**
 * Check if a given text contains emojis
 * @param text
 * @returns {boolean}
 */
function containsEmoji(text) {
    // Basic regex to match emoji ranges
    const emojiPattern = /\p{Emoji_Presentation}/u;
    return emojiPattern.test(text);
}

loginForm.addEventListener('submit', function (e) {
    if (containsEmoji(usernameInput.value)) {
        e.preventDefault();
        emailError.textContent = 'Email address must be in the form ‘jane@doe.nz';
        emailError.style.display = 'block';
    } else {
        emailError.style.display = 'none';
    }
});


