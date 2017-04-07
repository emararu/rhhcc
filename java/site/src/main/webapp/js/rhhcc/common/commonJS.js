/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var commonJSPrototype = {
    init: function(path) {
        var re = new RegExp("/", "g");
        this.path = "/" + path.replace(re, "");
    },
    
    url: function(path) {
        return this.path + path;
    },
    
    windowOpen: function(url, height, width) {
        window.open(url, '', 'width='+width+',height='+height+',left='+((window.innerWidth-width)/2)+',top='+((window.innerHeight-height)/2)+',menubar=no,toolbar=no,location=no');
    },
        
    showMessage: function(style, text) {    
        if (text != "") {
            var message = $("#message");
            message.removeClass("error ok").addClass(style);
            message.show();
            message.html(text); 
            setTimeout(function(){ message.fadeOut('fast'); }, 2000);    
        }              
    },
    

    swapMenuAuth: function(text) {    
        $.ajax({
            dataType: "html", 
            method: "POST",
            url: this.url("/user/auth/tile-icon-auth")
        }).done(function(icon) {
            $("#menu-main-auth-item").html(icon);
        }).fail(function(jqXHR, textStatus, errorThrown) {
            this.showMessage("error", jqXHR.status + ": " + errorThrown);
        }).then(
            function() {
                $.ajax({
                    dataType: "html", 
                    method: "POST", 
                    url: cjs.url("/user/auth/tile-menu-auth")
                }).done(function(menu) {
                    $("#menu-main-auth").html(menu); 
                    this.showMessage("ok", text);
                }).fail(function(jqXHR, textStatus, errorThrown) {
                    this.showMessage("error", jqXHR.status + ": " + errorThrown);
                });
            },
            function() {
            }
        );    
    },
    
    hideMenuAuth: function () {
        $("#menu-main-auth-item").removeClass("selected");
        $("#menu-main-auth").hide();
    },
    
    getFormData: function(url, field, mandatory) {
        var check = 0;
        for (var m = 0; m < mandatory.length; m++) {
            var f = $("#"+mandatory[m]);
            alert(mandatory[m] + "=" + f.val() + ", " + f.attr("placeholder"));
            if (f.val() == "") {
                f.addClass("red");
                check++;
            }
        }    
        if (check == 0) {

        } else {
            this.showMessage("error", "Не все обязательные поля заполнены!");
        }
    }    
};

function CommonJS(path) {
    function F() {};
    F.prototype = commonJSPrototype;
    var f = new F();
    f.init(path);
    return f;
}