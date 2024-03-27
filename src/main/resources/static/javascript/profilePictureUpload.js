document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('profilePicture').addEventListener(
        'click', () => document.getElementById('profilePictureInput').click()
    );

    document.getElementById('profilePictureInput').addEventListener(
        'change', () => document.getElementById('uploadProfileImage').submit()
    );
});