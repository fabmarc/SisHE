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

/* Alterar formato do calendar para pt */
PrimeFaces.locales['pt'] = {
        closeText: 'Fechar',
        prevText: 'Anterior',
        nextText: 'Pr&#243;ximo',
        currentText: 'Come&#231;o',
        monthNames: ['Janeiro','Fevereiro','Mar&#231;o','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
        monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun', 'Jul','Ago','Set','Out','Nov','Dez'],
        dayNames: ['Domingo','Segunda','Ter&#231;a','Quarta','Quinta','Sexta','S&#225;bado'],
        dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','S&#225;b'],
        dayNamesMin: ['D','S','T','Q','Q','S','S'],
        weekHeader: 'Semana',
        firstDay: 0,
        isRTL: false,
        showMonthAfterYear: false,
        yearSuffix: '',
        timeOnlyTitle: 'S&#243; Horas',
        timeText: 'Tempo',
        hourText: 'Hora',
        minuteText: 'Minuto',
        secondText: 'Segundo',
        currentText: 'Data Atual',
        ampm: false,
        month: 'M&#234;s',
        week: 'Semana',
        day: 'Dia',
        allDayText : 'Todo Dia'
    };

