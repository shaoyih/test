$(window).on("scroll", function() {
			if($(window).scrollTop()) {
						$('nav').addClass('black');
			}

			else {
						$('nav').removeClass('black');
			}
})

function allGenres(resultData){
	
	let genreSection=jQuery("#genreDropDown");

	let rowHTML="";
	
	for (let i=0;i<resultData.length;i++){
		rowHTML+="<li><a href='index.html?by=browse&page=1&limit=10&genre="+resultData[i]["name"]+"'>"+resultData[i]["name"]+"</a></li>";
		
	}
	genreSection.append(rowHTML);
	
}

function allAlpha(){
	let alphaSection=jQuery("#titleDropDown");
	
	let alphas=[ 'A', 'B', 'C', 'D', 'E', 'F', 
		'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 
		'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z','0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];
	let rowHTML="";
	for (let i=0;i<alphas.length;i++){
		rowHTML+="<li><a href='index.html?by=browse&page=1&limit=10&startsWith="+alphas[i]+"'>"+alphas[i]+"</a></li>";
		
	}
	alphaSection.append(rowHTML);
	
}
function submitSearchKey(formSubmitEvent){
    
    formSubmitEvent.preventDefault();
    query="index.html?by=search&"+$("#search_form").serialize()+"&page=1&limit=10";
    window.location.replace(query);
}
function submitSearchName(formSubmitEvent){
    console.log($("#search_form1").serialize());
    formSubmitEvent.preventDefault();
    query="index.html?by=search&"+$("#search_form1").serialize()+"&page=1&limit=10";
    window.location.replace(query);
}

jQuery.ajax({
	 dataType: "json",  // Setting return data type
	 method: "GET",// Setting request method
	 url: "project1/get_genres" , // Setting request url, which is mapped by StarsServlet in Stars.java
	 success: (resultData) => allGenres(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
	});
allAlpha();

$("#search_form").submit((event) => submitSearchKey(event));
$("#search_form1").submit((event) => submitSearchName(event));