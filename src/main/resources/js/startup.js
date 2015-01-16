
jQuery(function($) {
	//USADA PARA ACESSIBILIDADE NO ACCORDION DO BOOTSTRAP
	atualizarDivContentAccordion();
	
	//USADA PARA ACESSIBILIDADE NO MENU
	$(".dropdown-submenu").each(function() {
		$(this).mouseover(function() {
			esconderSubniveisMouseOver(this);//função do menu.js
		});
		$(this).keyup(function(event) {
			entrarNoSubnivel(this, event);//função do menu.js
		});
	});
	$(".dropdown-submenu").each(function() {
		$(this).mouseout(function() {
			esconderSubniveis(this);
		});
	});
	
	
	/**
	 * Desabilita o click nos commandLink que possuem esse class
	 */
	$(".disable-link").each(function() {
		$(this).attr('onclick', 'return false');
	});
	/**
	 * Desabilita o click nos commandLink que possuem esse class
	 */
	$(".disable-btn").each(function() {
		$(this).attr('onclick', 'return false');
	});
	
	/**
	 * Para todos os "a" será adicionado evento que guarda no ContextoController o id do componente clicado
	 */
	$("a").each(function(){
		var click = $(this).attr('onclick');
		if(click != null){
			if(click.indexOf("guardarIdComponenteController(this);") == -1){
				click = "guardarIdComponenteController(this);"+click;
				$(this).attr('onclick', click);
			}
		}
	});
	
	$("input").each(function(){
		var click = $(this).attr('onclick');
		if(click != null){
			if(click.indexOf("guardarIdComponenteController(this);") == -1){
				click = "guardarIdComponenteController(this);"+click;
				$(this).attr('onclick', click);
			}
		}
	});
	
	/*$("button").each(function(){
		var click = $(this).attr('onclick');
		if(click != null){
			if(click.indexOf(",oncomplete:function(xhr,status,args){updateA();}});") == -1) {
				var stringSubstituida = "});";
				var oncomplete = ",oncomplete:function(xhr,status,args){updateA();}});";
				var novoclick = click.replace(stringSubstituida, oncomplete);
				
				$(this).attr('onclick', novoclick);
			}
		}
	});*/
	
    loadMasks();
    aplicarZoom();
});

function atualizarDivContentAccordion() {
	$(".accordion-body").each(function() {
		exibeEscondeDivContentAccordionIn(this);//função do bootstrap.js
	});
}

/**
 * Atualiza o evento onclik dos "a"
 */
function updateA(){
	$("a").each(function(){
		var click = $(this).attr('onclick');
		if(click != null){
			if(click.indexOf("guardarIdComponenteController(this);") == -1){
				click = "guardarIdComponenteController(this);"+click;
				$(this).attr('onclick', click);
			}
		}
	});
}

function onRenderAjax() {
	attachFunctionGuardarIdComponente();
	removerDivFileUpload();
	loadMasks();
}

function attachFunctionGuardarIdComponente() {
	$("a").each(function(){
		var click = $(this).attr('onclick');
		if(click != null){
			if(click.indexOf("guardarIdComponenteController(this);") == -1){
				click = "guardarIdComponenteController(this);"+click;
				$(this).attr('onclick', click);
			}
		}
	});
	
	$("input").each(function(){
		var click = $(this).attr('onclick');
		if(click != null){
			if(click.indexOf("guardarIdComponenteController(this);") == -1){
				click = "guardarIdComponenteController(this);"+click;
				$(this).attr('onclick', click);
			}
		}
	});
}

function removerDivFileUpload() {
	$( ".ui-fileupload-content" ).remove();
}
