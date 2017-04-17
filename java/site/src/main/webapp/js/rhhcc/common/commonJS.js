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
        var m = this;
        $.ajax({
            dataType: "html", 
            method: "POST",
            url: m.url("/user/auth/tile-icon-auth")
        }).done(function(icon) {
            $("#menu-main-auth-item").html(icon);
        }).fail(function(jqXHR, textStatus, errorThrown) {
            m.showMessage("error", jqXHR.status + ": " + errorThrown);
        }).then(
            function() {
                $.ajax({
                    dataType: "html", 
                    method: "POST", 
                    url: m.url("/user/auth/tile-menu-auth")
                }).done(function(menu) {
                    $("#menu-main-auth").html(menu); 
                    m.showMessage("ok", text);
                }).fail(function(jqXHR, textStatus, errorThrown) {
                    m.showMessage("error", jqXHR.status + ": " + errorThrown);
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
    
    checkFormMandatory: function(field) {
        var check = 0;
        for (var i = 0; i < field.length; i++) {
                var f = $("[name='" + field[i] + "']");
            if (f.val() == "") {
                f.addClass("red");
                check++;
            }
        }
        if (check == 0) {
            return true;
        } else {
            this.showMessage("error", "Не все обязательные поля заполнены!");
            return false;
        } 
    },
    
    sendFormData: function(form, field, text) { 
        var a = form.attr("action");
        var m = this;
        
        if (History.enabled) {
            var d = {}; 
            for (var i = 0; i < field.length; i++) {
                var f = $("[name='" + field[i] + "']");
                if (f.val() != "") {
                    d[field[i]] = f.val();
                }
            }            
            console.log(JSON.stringify(d));            
            $.ajax({
                contentType: "application/json; charset=utf-8",
                dataType: "html",
                mimeType: "application/json",
                data: JSON.stringify(d),
                method: "POST", 
                url: a
            }).done(function(data) {
                History.pushState(null, text, m.url("/complete"));
                $("div.main").scrollTop(0);
                $("#content").html(data);
            }).fail(function(jqXHR, textStatus, errorThrown) {
                m.showMessage("error", jqXHR.status + ": " + errorThrown);
            });
        } else {
            form.attr("action", a+"/submit?_csrf=" + $.cookie("XSRF-TOKEN")).submit();
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