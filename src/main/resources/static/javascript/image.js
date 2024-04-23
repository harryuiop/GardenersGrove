function displayImage() {
    const input = document.getElementById('plantImage');
    const imgs = document.querySelectorAll('.selectedImage');
    const file = input.files[0];
    const reader = new FileReader();

    reader.onload = function (e) {
        for (const img of imgs) {
            img.src = e.target.result;
        }
    }

    if (file) {
        reader.readAsDataURL(file);
    }
}