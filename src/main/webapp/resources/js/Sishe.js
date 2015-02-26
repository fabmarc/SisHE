function atualizarSaldo(){
	var tam = document.getElementById("divSaldo").offsetWidth;
	if(tam >= 200){
		document.getElementById("logoutForm:btnAtualizarSaldo").click();
	}
}

function obterValorDoComponente(id) {
	var temp;
	temp = document.getElementById(id);
	return temp.value;
}