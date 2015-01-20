$(document).ready(
	function onCompleteDataTable(){
		
		$(".togglerManager").click(function(){
			//Verifica se o campo está expandido
			var estaExpandido = false;
			estaExpandido= ($(this).parent().parent().parent().find(".ui-expanded-row-content").length > 0) ? true:false;
			//verifica se algum checkbox de equipe técnica está marcado		
			var algumMarcado = false;
			$(".togglerManager").each(function(){
				  if($(this).find("input:checkbox").is(":checked"))
					  algumMarcado = true;
			});
			//Se algum marcado E não expandido OU nenhum marcado E expandido
			if(algumMarcado != estaExpandido){//(x^¬y) V (¬x^y)
				$("td .togglerManagerColumn").find(".ui-row-toggler").click();
			}
		});
		
		//Executado uma vez após 3 segundos de aberturada da tela para expandir caso 
		var execute = setInterval(
				function(){
					$(".togglerManager").each(function(){
						if($(this).find("input:checkbox").is(":checked") ){
							$("td .togglerManagerColumn").find(".ui-row-toggler").click();
						}
						window.clearTimeout(execute);
					});
			},2000);
	}
);