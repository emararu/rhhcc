/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(function() {

    $("#create").click(function() {   
        if (cjs.checkFormMandatory(["login", "password"])) {
            cjs.sendFormData(
                $("#register_do"), 
                ["firstname", "lastname", "email", "phone"], 
                //["login", "password", "firstname", "lastname", "birthday", "email", "phone"], 
                "textMessage"
            );
        }
    });  


});
