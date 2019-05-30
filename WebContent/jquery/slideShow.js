function slideBanner(element,max) { //Shake the window
	element = element == 0 ? 1 : element;
	var SliderElemetn1='$imgSlideShows_'+element;
	var prox=parseInt(element)+1;
	var SliderElemetn2='$imgSlideShows_'+prox;
	SliderElemetn1.replace("_'","_");
	SliderElemetn2.replace("_'","_");
	jq(SliderElemetn1).stop();
	jq(SliderElemetn1).hide().fadeOut();
	
	if(element.toString()==max){
		var ElementAux=element-1;
		var elementPos=element-ElementAux;
		var SliderElemetn2='$imgSlideShows_'+1;
		SliderElemetn2.replace("_'","_");
		jq(SliderElemetn2).stop();
		jq(SliderElemetn2).hide().fadeIn(2500); 
	}
	else{
		jq(SliderElemetn2).stop();
		jq(SliderElemetn2).hide().fadeIn(2500); 	
	}
	jq(SliderElemetn2).promise().done()
}

function antSlideBanner(element,max) { 
	element = element == 0 ? 1 : element;
	var SliderElemetn1='$imgSlideShows_'+element;
	var prox=parseInt(element)-1;
	var SliderElemetn2="";
	if(prox<=0){
		element=max;
		
		SliderElemetn2='$imgSlideShows_'+element;	
	}else{
		SliderElemetn2='$imgSlideShows_'+prox;	
	}
	SliderElemetn1.replace("_'","_");
	SliderElemetn2.replace("_'","_");
	jq(SliderElemetn1).stop();
	jq(SliderElemetn1).hide().fadeOut();
	var min=1;
	if(element.toString()==min){
		var ElementAux=element+1;
		var elementPos=element+ElementAux;
		var SliderElemetn2='$imgSlideShows_'+max;
		SliderElemetn2.replace("_'","_");
		jq(SliderElemetn2).stop();
		jq(SliderElemetn2).hide().fadeIn(2500); 
	}
	else{
		jq(SliderElemetn2).stop();
		jq(SliderElemetn2).hide().fadeIn(2500); 	
	}
	jq(SliderElemetn2).promise().done()
}
