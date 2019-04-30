/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var checked = false;
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


