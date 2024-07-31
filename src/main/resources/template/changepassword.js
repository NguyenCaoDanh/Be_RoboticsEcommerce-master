var password = document.getElementById("password")
    , confirm_password = document.getElementById("confirmPassword");
document.getElementById('signupLogo').src = "./img/user.png";
enableSubmitButton();
var form = document.getElementById('signupForm');

function validatePassword() {
    if(password.value != confirm_password.value) {
        confirm_password.setCustomValidity("Mật khẩu không trùng khớp");
        return false;
    } else {
        confirm_password.setCustomValidity('');
        return true;
    }
}

password.onchange = validatePassword;
confirm_password.onkeyup = validatePassword;

function enableSubmitButton() {
    document.getElementById('submitButton').disabled = false;
    document.getElementById('loader').style.display = 'none';
}

function disableSubmitButton() {
    document.getElementById('submitButton').disabled = true;
    document.getElementById('loader').style.display = 'unset';
}

function validateSignupForm() {
    for(var i=0; i < form.elements.length; i++){
        if(form.elements[i].value === '' && form.elements[i].hasAttribute('required')){
            console.log('Vui lòng nhập trường này!');
            return false;
        }
    }

    if (!validatePassword()) {
        return false;
    }

    onSignup();
}

form.addEventListener("submit", function (event) {
    event.preventDefault();
    validateSignupForm();
})

function onSignup() {
    // var xhttp = new XMLHttpRequest();
    // xhttp.onreadystatechange = function() {
    //
    //     disableSubmitButton();
    //
    //     if (this.readyState == 4 && this.status == 200) {
    //         enableSubmitButton();
    //     }
    //     else {
    //         console.log('AJAX call failed!');
    //         setTimeout(function(){
    //             enableSubmitButton();
    //         }, 1000);
    //     }
    //
    // };
    // // xhttp.open("PUT", "http://localhost:8090/email/reset-password", false);
    // xhttp.addEventListener("load", function () {
    //     console.log(xhttp.status);
    //     console.log(xhttp.responseText);
    // });
    // xhttp.addEventListener("error", function () {
    //     console.log("Error occurred!");
    // });
    // xhttp.send(null);

    var url = new URL(window.location.href);

    // for (var i = 0; i < url.searchParams.values(); i++) {
    //     console.log(url.searchParams.values()[i]);
    // }

    var token = Array.from(url.searchParams.values())[0];

    var fetchUrl = "http://localhost:8081/email/reset-password?token=" + token;

    // fetch(fetchUrl, {
    //     method: "POST",
    //     body: JSON.stringify({password}),
    //     headers: {
    //         "Content-Type": "application/json",
    //         "Access-Control-Allow-Origin": "*"
    //     }
    // })
    //     .then(function (response) {
    //         console.log(response);
    //         if (response.ok) {
    //             window.location="ajax_info.html";
    //         }
    //     })
    //     .catch(function (error) {
    //         console.log(error);
    //     })


    const options = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({password: password.value})
    };

    fetch(fetchUrl, options)
        .then(function (response) {
            console.log(response);
            if (response.ok) {
                window.location="ajax_info.html";
            }
        })
        .catch(function (error) {
            console.log(error);
        })
}
