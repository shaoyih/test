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
function handleResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");

    
    // Find the empty table body by id "star_table_body"
    let starTableBodyElement = jQuery("#movie_table_body");

    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < resultData.length; i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            // Add a link to single-star.html with id passed with GET url parameter
            
             resultData[i]["title"]
            "</th>";
        rowHTML += "<th>" + resultData[i]["movie_id"] + "</th>";
        rowHTML += "<th>" + resultData[i]["rating"] + "</th>";
        rowHTML += "<th>" + resultData[i]["year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["director"] + "</th>";
        rowHTML+= getGenres(resultData[i]["genres"]);
        rowHTML+= getStars(resultData[i]["stars"]);
        rowHTML +="<th>"+'</a>' +"<br> <button type='button' class='btn btn-primary btn-lg' value='"+resultData[i]["title"]+"'>add to cart</button>"+"</th>";
        rowHTML += "</tr>";
        console.log(rowHTML);
        
        
        // Append the row created to the table body, which will refresh the page
        starTableBodyElement.append(rowHTML);
    }
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

//handle button click 

$(document).on('click', 'button',function(){
	rawUrl="shoppingCart.html?movie=";
	rawUrl+=this.value;
	window.location.replace(rawUrl);
});


let movieId = getParameterByName('id');


//Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
 dataType: "json",  // Setting return data type
 method: "GET",// Setting request method
 url: "project1/single_movie?id=" + movieId, // Setting request url, which is mapped by StarsServlet in Stars.java
 success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});