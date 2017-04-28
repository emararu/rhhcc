/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(function() {

    $("#create").click(function() {   
        if (cjs.checkFormMandatory(["login", "password", "gender"])) {
            if (cjs.checkFormatDate(["birthday"])) {
                cjs.sendFormData(
                    $("#register_do"),
                    ["login", "password", "firstname", "lastname", "gender", "birthday", "email", "phone"]
                );
            }
        }
    });  


});
