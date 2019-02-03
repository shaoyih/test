/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");
    
    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function getGenres(array){
	let rowHTML = "";
	rowHTML += "<th>";
	for (let i = 0; i < array.length; i++) {
		rowHTML+=array[i];
		rowHTML+="<br>";
		
	}
	rowHTML += "</th>";
	return rowHTML;
}
function getStars(array){
	let rowHTML = "";
	rowHTML += "<th>";
	for (let i = 0; i < array.length; i++) {
		rowHTML +=
            
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="single_star.html?id=' + array[i][1] + '">'
            + array[i][0] +     // display star_name for the link text
            '</a>' ;
		rowHTML+="<br>";
		
		
	}
	rowHTML += "</th>";
	return rowHTML;
}
function handleStarResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");

    
    // Find the empty table body by id "star_table_body"
    let starTableBodyElement = jQuery("#movie_table_body");
    let totalPage=resultData[0]["totalPage"];
    madePagination(totalPage);
    madeSort();
    // Iterate through resultData, no more than 10 entries
    
    for (let i = 1; i < resultData.length; i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="single_movie.html?id=' + resultData[i]['id'] + '">'
            + resultData[i]["title"] +"</th>";
        rowHTML += "<th>" + resultData[i]["rating"] + "</th>";
        rowHTML += "<th>" + resultData[i]["year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["director"] + "</th>";
        rowHTML+= getGenres(resultData[i]["genres"]);
        console.log(resultData[i]["genres"]);
        rowHTML+= getStars(resultData[i]["stars"]);
        rowHTML+="<th>"+'</a>' +"<br> <button type='button' class='btn btn-primary btn-lg' value='"+resultData[i]["title"]+"'>add to cart</button>"+"</th>"
        rowHTML += "</tr>";
       
        // Append the row created to the table body, which will refresh the page
        starTableBodyElement.append(rowHTML);
    }
    
}
function madeSort(){
	
	let Dropdown=jQuery("#orderDropDown");
	let order=["rateA","rateD","titleA","titleD"];
	for(let i=0;i<order.length;i++){
		Dropdown.append(getOrderHtml(order[i]));
	}
}

function getOrderHtml(order){
	let current=getParameterByName('order');
	var baseUrl=location.href;
	console.log("order testing")
	console.log(current);
	if(current){
		baseUrl=baseUrl.replace(current,order);
	}
	else{
		baseUrl=String(baseUrl)+"&order="+order;
	}
	
	return "<li><a href='"+baseUrl+"'>"+order+"</a></li>";
}
function madePagination(total){
	let current=getParameterByName('page');
	let pageTableBodyElement = jQuery("#pagination");
	let limit=getParameterByName('limit');
	let totalPage=Math.ceil(total/limit);
	let prev=getPrevHTML(current,totalPage);
	let next=getNextHTML(current,totalPage);
	pageTableBodyElement.append(prev);
	pageTableBodyElement.append(next);
	
	
	
	
}
function getPrevHTML(current,totalPage){
	if (current=="1"){
		return "";
	}
	
	let offset = (parseInt(current)-1).toString();
	
	var baseUrl = location.href.replace("page="+current, "page="+offset);
	return "<li ><a  href='"+baseUrl+"'>Previous</a></li>";
}
function getNextHTML(current,totalPage){
	if (current==totalPage.toString()){
		return "";
	}
	
	let offset = (parseInt(current)+1).toString();
	
	
	var baseUrl = location.href.replace("page="+current, "page="+offset);
	console.log(baseUrl);
	return "<li ><a  href='"+baseUrl+"'>Next</a></li>";
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */


let offset=getParameterByName('page');
let order=getParameterByName('order');
let limit=getParameterByName('limit');

let mode=getParameterByName("by");
if (mode=="browse"){
	let parameter = getParameterByName('startsWith');
	

	
	
	
	if (parameter){
		
		jQuery.ajax({
		    dataType: "json", // Setting return data type
		    method: "GET", // Setting request method
		    url: "project1/movies?by=browse&startsWith=" + parameter+"&order="+order+"&limit="+limit+"&page="+offset, // Setting request url, which is mapped by StarsServlet in Stars.java
		    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
		});
		
	}
	else{
		parameter = getParameterByName('genre');
		
		jQuery.ajax({
		    dataType: "json", // Setting return data type
		    method: "GET", // Setting request method
		    url: "project1/movies?by=browse&genre=" + parameter+"&order="+order+"&limit="+limit+"&page="+offset, // Setting request url, which is mapped by StarsServlet in Stars.java
		    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
		});
	}
}
else{
	let title=getParameterByName('title');
	let year=getParameterByName('year');
	let director=getParameterByName('director');
	let star=getParameterByName('stars');
	console.log("into it")
	jQuery.ajax({
	    dataType: "json", // Setting return data type
	    method: "GET", // Setting request method
	    url: "project1/movies?by=search&title=" + title+"&year="+year+"&director="+director+"&stars="+star+"&order="+order+"&limit="+limit+"&page="+offset, 
	    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
	});

	
}
$(document).on('click', 'button',function(){
	rawUrl="shoppingCart.html?movie=";
	rawUrl+=this.value;
	window.location.replace(rawUrl);
}
)
//navbar part
function allGenres(){
	let genreSection=jQuery("#genreDropDown");
	console.log(genreSection);
	let genres=['Action','Adult','Adventure','Animation','Biography',
		'Comedy','Crime','Documentary','Drama','Family','Fantasy','History',
		'Horror','Music','Musical','Mystery','Reality-TV','Romance','Sci-Fi',
		'Sport','Thriller','War','Western'];
	let rowHTML="";
	for (let i=0;i<genres.length;i++){
		rowHTML+="<li><a href='index.html?by=browse&page=1&limit=10&genre="+genres[i]+"'>"+genres[i]+"</a></li>";
		
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
allAlpha();
allGenres();

$(window).on("scroll", function() {
	if($(window).scrollTop()) {
				$('nav').addClass('black');
	}

	else {
				$('nav').removeClass('black');
	}
})

$(document).ready(function() {
	var someVarName = localStorage.getItem("someVarKey");
	$('#sel1 option[value="'+someVarName+'"]').attr('selected',true);
	console.log($('#sel1 option:contains("'+someVarName+'")'));
	jQuery("#sel1").change(function() {
	
	
	let limit=getParameterByName('limit');
	var select = $("#sel1 option:selected").val();
	localStorage.setItem("someVarKey", select);
	
	var newUrl = location.href.replace("limit="+limit, "limit="+select);
	window.location.replace(newUrl);
//	window.location.href.replace("limit="+limit, "limit="+select);
//	location.reload();
	
});
});

