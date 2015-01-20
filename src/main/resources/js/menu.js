// up - 38
// down - 40
// left - 37
// right - 39
// tab - 9
// event.wich -> IE
//event.keyCode -> Chrome e Firefox
//

/**
 * Função que faz o Submenu aparecer se a tecla right for pressionada
 * @param element 
 * @returns event
 */
function entrarNoSubnivel(element, event) {
	var tecla = event.wich;
	
	if (tecla == undefined) { 
		tecla = event.keyCode;
	}
	
	if(tecla == 39){
		esconderSubniveis(element);
		var ul = element.getElementsByTagName('ul')[0];
		if (ul){
			ul.style.display = 'block';
		}
	}else if(tecla == 37){
		esconderSubniveis(element);
	}
}

/**
 * Função para esconder os submenus
 * @param element
 */
function esconderSubniveis(element){
	var parent = element.parentElement;
	var children = parent.getElementsByTagName('ul');
	if(children){
		for(var i = 0; i < children.length; ++i){
			children[i].style.display = 'none';
		}
	}
}

/**
 * Função para sair do submenu quando a tecla left for pressionada
 * @param element
 * @param event
 */
function sairDoSubNivel(element, event){
	var tecla = event.wich;
	
	if (tecla == undefined) {
		tecla = event.keyCode;
	}

	if(tecla == 37){
		var parent = element.parentElement; 
		if (parent){
			element.style.display = 'none';
			parent.focus();
		}
	}
}

function esconderSubniveisMouseOver(element){
	 esconderSubniveis(element);
	 var ul = element.getElementsByTagName('ul')[0];
	if (ul){
		ul.style.display = 'block';
	}
}
