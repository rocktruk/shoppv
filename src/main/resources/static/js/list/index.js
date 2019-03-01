$(function(){
	// 工具提示
	 $('[data-toggle="tooltip"]').tooltip();
	 $("#more_case").click(function(){
	 	$("#mycaseJ").parent().toggle(1000);
	 	$("#mycaseP").parent().toggle(1500);
	 })
})