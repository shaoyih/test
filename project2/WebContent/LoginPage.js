
function makeDecision(result){
	console.log("Start making decisions.")
	Jresult=JSON.parse(result);
	
	if (Jresult["status"]==="success"){
		window.location.replace("index.html");
	}
	else{
		$("#login_error_message").text(Jresult["message"]);
	}
	
}


function getResponse(info){
	console.log("subimit button clicked");
	info.preventDefault();
	console.log("subimit button clicked");
	$.post("raw/login",$("#account_form").serialize(),(result)=>makeDecision(result));
	console.log("subimit button clicked");
}

console.log("into js");
$("#account_form").submit((info)=>getResponse(info));