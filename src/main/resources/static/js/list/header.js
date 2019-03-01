$(function(){
	var headerJs = $("<div  class='header-js navbar-inverse text-default xjp-bg-default '>"+
		"<div class='container '>"+
 		"	<section class='header-top row header-height-default'>"+
 				"<div class='col-xs-6'>"+
 					"<span class='iconfont icon-fangzi-copy'></span>"+
 					"<span>XJP</span>"+
 				"</div>"+
 				"<div class='col-xs-6 text-right'>"+
 					"<span class='iconfont icon-dianhua1'></span>"+
 					"<span>17773917504</span>"+
 				"</div>"+
 			"</section>"+

 		"</div>"+
	"</div>");
	var navJs = $("<nav class='nav-js navbar navbar-default '>"+
		"<div class='container'>"+
			"<div class='row'>"+
				"<div class='navbar-header '>"+
					" <a class='navbar-brand logo' href='index.html'>"+
					 	"XJP团队"+
					" </a>"+
					" <button type='button' class='navbar-toggle collapsed' data-toggle='collapse' data-target='#mynav' aria-expanded='false'>"+
				       " <span class='sr-only'>Toggle navigation</span>"+
				        "<span class='icon-bar'></span>"+
				        "<span class='icon-bar'></span>"+
				       " <span class='icon-bar'></span>"+
				     " </button>"+
				"</div>"+
				"<div class='navbar-collapse collapse' id='mynav'>"+
					"<ul class='nav navbar-nav'>"+
						"<li class='active'><a href='index.html'>首页</a></li>"+
						"<li class=''><a href='team.html'>团队简介</a></li>"+
						"<li class=''><a href='new.html'>最新动态</a></li>"+
						"<li class=''><a href='service.html'>技术服务</a></li>"+
					"</ul>"+
				
				"</div>"+
			"</div>"+
		"</div>"+
	"</nav>");
	var bannerJs = $("<div class='banner banner-js ' data-interval='2000'>"+
		"<div class=''>"+
			"<div class='rol'>"+
				"<div class=''>"+
					"<div class='carousel slide' id='myCarousel'  data-ride='carousel' data-interval='2000'>"+
							"<!-- 列表 -->"+
						"	<ul class='carousel-indicators'>"+
								"<li data-target='#myCarousel' data-slide-to='0' class='active'></li>"+
								"<li data-target='#myCarousel' data-slide-to='1'></li>"+
								
							"</ul>"+
							"<!-- 图片 -->"+
							"<div class='carousel-inner '>"+
								"<div class='item active'>"+
									"<a href='javascript:void(0)' >"+
										"<img src='images/banner-1.png' alt=''  >"+
									"</a>"+
								"</div>"+
								"<div class='item '>"+
									"<a href='javascript:void(0)' >"+
										"<img src='images/banner-2.png' alt=''  />"+
									"</a>"+
								"</div>"+
							"</div>"+
							"<!-- 方向 -->"+
							"<div>"+
							"	<a href='#myCarousel' class='carousel-control left' data-slide='prev'>"+
									"<span class='glyphicon glyphicon-chevron-left'></span>"+
								"</a>"+
								"<a href='#myCarousel' class='carousel-control right' data-slide='next'>"+
									"<span class='glyphicon glyphicon-chevron-right'></span>"+
								"</a>"+
							"</div>"+
					"</div>"+
				"</div>"+
			"</div>"+
		"</div>"+
	"</div>");
	$("#header").append(headerJs);
	$("#nav").append(navJs);
	$("#headerbanner").append(bannerJs);
})