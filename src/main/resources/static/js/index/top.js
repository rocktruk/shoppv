var sEar = document.getElementById("search");
var top_div = document.getElementById( "back_top" );
	window.onscroll = function(){
		var h = document.body.scrollTop;
		//console.log(h);
		if (h>0)
		{
			sEar.style.background = "rgba(255,255,255,0.85)";
		}else if(h==0){
			sEar.style.background = "rgba(255,255,255,0)";
		}
		if (h>300)
		{
			top_div.style.opacity = "1";
		}else{
			top_div.style.opacity = "0";
		}
	}
	top_div.onclick = function(){
		document.body.scrollTop = 0;
	}
	
