function handleLoginResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

 
    if (resultDataJson["status"] === "success") {
        window.location.replace("dashboard.html");
    } else {
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#login_error_message").text(resultDataJson["message"]);
    }
}


function submitLoginForm(formSubmitEvent) {
    console.log("submit login form");
    console.log($("#login_form").serialize());
    formSubmitEvent.preventDefault();
    $.post(
    	"api/employee_login",
        $("#login_form").serialize(),
        (resultDataString) => handleLoginResult(resultDataString)
    );
}

// Bind the submit action of the form to a handler function
$("#login_form").submit((event) => submitLoginForm(event));