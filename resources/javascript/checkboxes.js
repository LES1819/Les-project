/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var checked = false;

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var checked = false;
function toggleAll2(){
           var checkboxes = document.getElementsByClassName('check[]');
           if(checked){
               for(var i = 0; i < checkboxes.length; i++){
                	checkboxes[i].checked = false;
              	}
           }
           else{
               for(var i = 0; i < checkboxes.length; i++){
               		checkboxes[i].checked = true;
            	}
           }
           checked = !checked;
           toggle();
        }
        
function toggle(){
        var checkboxes = document.getElementsByClassName('check[]');
        var checked = 0;
        for(var i = 0; i < checkboxes.length; i++){
            if(checkboxes[i].checked === true){
                checked++;
            }
            if(checked > 1){
               break;
            }
        }
	if(checked === 1){
            document.getElementsByClassName("check btn btn-danger")[0].disabled = false;
            document.getElementById("exampleModalLabel").innerHTML = "Apagar a atividade?";
            document.getElementById("modalid").innerHTML = "Irá apagar as cópias desta atividade e as suas associações a papéis,produtos,processos e padrões.";
	}
        else if(checked > 1){
            document.getElementsByClassName("check btn btn-danger")[0].disabled = false;
            document.getElementById("exampleModalLabel").innerHTML = "Apagar as atividades?";
            document.getElementById("modalid").innerHTML = "Irá apagar as cópias destas atividades e as suas associações a papéis,produtos,processos e padrões.";
        }
	else{
           document.getElementsByClassName("check btn btn-danger")[0].disabled = true;
	}
}

function toggleAll() {
    var checkboxes = document.getElementsByClassName('check[]');
    if (checked) {
        for (var i = 0; i < checkboxes.length; i++) {
            checkboxes[i].checked = false;
        }
    } else {
        for (var i = 0; i < checkboxes.length; i++) {
            checkboxes[i].checked = true;
        }
    }
    checked = !checked;
}

function enableAssociate(){
    var checkboxes = document.getElementsByClassName('check[]');
    var check = false;
    for(var i = 0; i < checkboxes.length; i++){
        if(checkboxes[i].checked === true){
            check = true;
            break;
        }
    }
    if(check === true)
        document.getElementsByClassName("btn btn-primary")[0].disabled = false;
    else
        document.getElementsByClassName("btn btn-primary")[0].disabled = true;
}

function toggleproduto(){
        var checkboxes = document.getElementsByClassName('check[]');
        var checked = 0;
        for(var i = 0; i < checkboxes.length; i++){
            if(checkboxes[i].checked === true){
                checked++;
            }
            if(checked > 1){
               break;
            }
        }
	if(checked === 1){
            document.getElementsByClassName("check btn btn-danger")[0].disabled = false;
            document.getElementById("exampleModalLabel").innerHTML = "Apagar o Produto?";
            document.getElementById("modalid").innerHTML = "Irá apagar a associção á atividade.";
	}
        else if(checked > 1){
            document.getElementsByClassName("check btn btn-danger")[0].disabled = false;
            document.getElementById("exampleModalLabel").innerHTML = "Apagar os produtos?";
            document.getElementById("modalid").innerHTML = "Irá apagar as associções ás atividades.";
        }
	else{
           document.getElementsByClassName("check btn btn-danger")[0].disabled = true;
	}
}


