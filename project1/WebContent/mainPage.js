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
function getGenres(){
	let genreSection=jQuery("#genreDropDown");
	console.log(genreSection);
	let genres=['Action','Adult','Adventure','Animation','Biography',
		'Comedy','Crime','Documentary','Drama','Family','Fantasy','History',
		'Horror','Music','Musical','Mystery','Reality-TV','Romance','Sci-Fi',
		'Sport','Thriller','War','Western'];
	rowHTML="";
	for (let i=0;i<genres.length;i++){
		rowHTML+="<li><a href='index.html?by=browse&offset=0&limit=10&genre="+genres[i]+"'>"+genres[i]+"</a></li>";
		
	}
	genreSection.append(rowHTML);
	
}
function getAlpha(){
	let alphaSection=jQuery("#titleDropDown");
	
	let alphas=[ 'A', 'B', 'C', 'D', 'E', 'F', 
		'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 
		'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z','0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];
	rowHTML="";
	for (let i=0;i<alphas.length;i++){
		rowHTML+="<li><a href='index.html?by=browse&offset=0&limit=10&startsWith="+alphas[i]+"'>"+alphas[i]+"</a></li>";
		
	}
	alphaSection.append(rowHTML);
	
}
getGenres();
getAlpha();

