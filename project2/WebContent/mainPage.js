

function manageResult(result){
	
	
	
}

function submitResult(info){
	console.log("button clicked");
	info.preventDefault();
	$.post("raw/main", $("#search_form").serialize(),(result)=>(manageResult(result)));
}

$("#search_form").submit((info)=>getResponse(info))