var myMap = new Map();


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

$('#autocomplete').autocomplete({
	lookup: function(query,doneCallback){
		if (query.length>=3){
			handleLookup(query,doneCallback);
		}
		
	},
	onSelect: function(suggestion) {
		handleSelectSuggestion(suggestion)
	},
// set delay time
	deferRequestBy: 300,
		
});

function handleLookup(query,doneCallback){
	console.log("starting search for autocomplete results")
	if (myMap.has(query)){
		console.log("using caching data");
		let data=myMap.get(query);
		console.log("cache",data);
		handleLookupCacheSuccess(data, query, doneCallback) ;
		return;
	}
	console.log("using ajax to get data");
	jQuery.ajax({
		"method": "GET",
		// generate the request url from the query.
		// escape the query string to avoid errors caused by special characters 
		"url": "project1/movies?by=search&"+$("#search_form1").serialize()+"&year=null&director=null&stars=null&order=null&page=1&limit=10",
		"success": function(data) {
			// pass the data, query, and doneCallback function into the success handler
			handleLookupAjaxSuccess(data, query, doneCallback) 
		},
		"error": function(errorData) {
			console.log("lookup ajax error")
			console.log(errorData)
		}
	})
	
}
function handleLookupCacheSuccess(data, query, doneCallback) {
	console.log("cached succeed",data);
	
	
	doneCallback( { suggestions: data } );
}
function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("ajax succeed",data);
	
	let total=[];
//	var obj = new Object();
//	   obj.name = "Raj";
//	   obj.age  = 32;
//	   obj.married = false;
//	   var jsonString= JSON.stringify(obj);
	
	//<10 11 would be equals to <=10
	let len=Math.min(data.length, 11);
	for (let i=1;i<len;++i){
		var obj = new Object();
		   obj.data = "single_movie.html?id="+ data[i]['id'];
		   obj.value  = data[i]["title"];
		  total.push(obj);
		   
	}
//	var jsonString= JSON.stringify(total);
	console.log(total);
	myMap.set(query,total);
	doneCallback( { suggestions: total } );
}

function handleSelectSuggestion(suggestion){
	window.location.replace(suggestion["data"]);
}