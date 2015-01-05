/* 
 *  Implementacao pertencente a criação de um scroll no topo de uma div
 *  */
$(document).ready(function() {
	$(".wrapper1").scroll(function(){
	    $(".wrapper2").scrollLeft($(".wrapper1").scrollLeft());
	});
	$(".wrapper2").scroll(function(){
		$(".wrapper1").scrollLeft($(".wrapper2").scrollLeft());
	});
});