$(document).ready(function (){

    // test script is working
    $('#fileImage').change(function () {
        showImageThumbnail(this);
    })

    // logs user out of session using link instead of button
    $('#logoutLink').on("click", function (e) {
        e.preventDefault();
        document.logoutForm.submit();
    });

});


console.log("Hello world!");

function showImageThumbnail(fileInput) {
    file = fileInput.files[0];
    reader = new FileReader();

    reader.onload = function (e){
        $('#thumbnail').attr('src', e.target.result);
    };

    reader.readAsDataURL(file);
}



function checkPasswordMatch(fieldConfirmPassword) {
    if (fieldConfirmPassword.value != $("#password").val()) {
        fieldConfirmPassword.setCustomValidity("Passwords do not match!");
    } else {
        fieldConfirmPassword.setCustomValidity("");
    }
}