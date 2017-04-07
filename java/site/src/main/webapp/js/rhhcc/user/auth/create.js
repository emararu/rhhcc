/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(function() {

    $("#create").click(function() {        
        cjs.getFormData(
            "url", 
            ["login", "password", "password_repeat", "firstname", "lastname", "birthday", "email", "phone"], 
            ["login", "password"]
        );
    });  


});
