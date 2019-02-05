$(window).on("scroll", function() {
			if($(window).scrollTop()) {
						$('nav').addClass('black');
			}

			else {
						$('nav').removeClass('black');
			}
})

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
allAlpha();
allGenres();
$("#search_form").submit((event) => submitSearchKey(event));
$("#search_form1").submit((event) => submitSearchName(event));