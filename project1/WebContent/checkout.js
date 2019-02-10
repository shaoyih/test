/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleLoginResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    console.log(resultDataString)
    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);
    console.log(resultDataJson["data"]);
    
    if(resultDataJson["status"]=="success"){
    $("#check_out").remove();
    rowHTML="";
    document.getElementById("whole-table").classList.remove("d-none");
    for (var key in resultDataJson["data"]) {
        if (resultDataJson["data"].hasOwnProperty(key)) {
            rowHTML+="<tr> <th scope='row'>"+key+"</th>  <td>";
            
            rowHTML+=resultDataJson["data"][key]+"<br>";
            
            rowHTML+="</td>";
            rowHTML+="<td>"+resultDataJson["data"][key].length.toString()+"</td>";
        }
        rowHTML+="</tr>";
    }
    $("#movie-table").append(rowHTML);
    }
    else{
    	//change the setting of the popup window
    	console.log("into wrong");
    	document.getElementById("wrongInfo").style.display="block";
    	
    }
}


function submitLoginForm(formSubmitEvent) {
    

    formSubmitEvent.preventDefault();
    console.log("submit login form");

    
    $.post(
        "project1/checkout",
        $("#check_out").serialize(),
        (resultDataString) => handleLoginResult(resultDataString)
    );
}

// Bind the submit action of the form to a handler function
$("#check_out").submit((event) => submitLoginForm(event));
$("#nav-bar1").load("navBar.html");
document.getElementsByClassName("close")[0].onclick=function(){
	document.getElementById("wrongInfo").style.display="none";
};
