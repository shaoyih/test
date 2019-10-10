
function handleStarResult(resultDataString){
	$("#feedback-message").text(resultDataString["message"]);
	document.getElementById("wrongInfo").style.display="block";
}

function handleMovieResult(resultDataString){
	
	
	$("#feedback-message").text(resultDataString["message"]);
	document.getElementById("wrongInfo").style.display="block";
	
	
}

//add star part
function submitStarForm(formSubmitEvent) {
    console.log("submit star form");
    console.log($("#add_star").serialize());
    formSubmitEvent.preventDefault();
    $.get(
    	"api/dashboard",
        "by=insertS&"+$("#add_star").serialize(),
        (resultDataString) => handleStarResult(resultDataString)
    );
}

function submitMovieForm(formSubmitEvent) {
    console.log("submit movie form");
    console.log($("#add_movie").serialize());
    formSubmitEvent.preventDefault();
    $.get(
    	"api/dashboard",
        "by=insertM&"+$("#add_movie").serialize(),
        (resultDataString) => handleMovieResult(resultDataString)
    );
}


// Bind the submit action of the form to a handler function
$("#add_star").submit((event) => submitStarForm(event));
$("#add_movie").submit((event) => submitMovieForm(event));

function handleTableInfo(resultData){
	let stats =jQuery("#tableStats");
	let rowHtml="<ul>";
	console.log(resultData);
	for(let i=1;i<resultData.length;i++){
		
		rowHtml+="<li><a href='#'>"+resultData[i]["name"]+"</a></li>";
		rowHtml+="<li><table class=\"table table-dark\">"+
				"<thead>"+
				"<tr>" +
				"<th>attribute</th>"+
				"<th>type</th>"+
				"</thead>"+
				"<tbody id='"+resultData[i]["name"]+"'>"+
					
				"</tbody>"+
				"</table></li>";
		
				let name=resultData[i]["name"];
				console.log("name is "+name);
				jQuery.ajax({
					dataType: "json",
					method: "get",
					url: "api/dashboard?by=get&type=a&name="+name,
					success: (resultData1)=>handleAttrInfo(resultData1,name)
				});
		
	}
	rowHtml+="</ul>"
	stats.append(rowHtml);
	
}
function handleAttrInfo(resultData,name){
	console.log(name);
	let stats =jQuery("#"+name);
	console.log(resultData);
	for(let i=0;i<resultData.length;i++){
		let rowHTML="<tr>";
		rowHTML+="<th>"+resultData[i]["name"]+"</th>";
		rowHTML+="<th>"+resultData[i]["type"]+"</th>";
		rowHTML+="</tr>";
		stats.append(rowHTML);
			
	}
	
}

	

jQuery.ajax({
	dataType: "json",
	method: "get",
	url: "api/dashboard?by=get&type=t",
	success: (resultData)=>handleTableInfo(resultData)
});

document.getElementsByClassName("close")[0].onclick=function(){
	document.getElementById("wrongInfo").style.display="none";
};

