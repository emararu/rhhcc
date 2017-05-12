/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(function() {

    $("#create").click(function() {   
        if (cjs.checkFormMandatory(["login", "password", "password_repeat", "firstname", "lastname", "gender", "email"])) {
            if (cjs.checkEqualPair("password", "password_repeat")) {
                if (cjs.checkFormatDate(["birthday"])) {
                    cjs.sendFormData(
                        $("#register_do"),
                        ["login", "password", "firstname", "lastname", "gender", "birthday", "email", "phone"]
                    );
                }
            }
        }
    });  


});
