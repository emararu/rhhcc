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
            setTimeout(function(){ message.fadeOut('fast'); }, 5000);    
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
    
    checkResultReturn: function(check, text) {        
        if (check == 0) {
            return true;
        } else {
            this.showMessage("error", text);
            return false;
        }
    },
    
    unmarkRedText: function(field) {
        field.removeClass("red");
        field.siblings("input").removeClass("red");
        field.off("keypress");        
    },
    
    unmarkRedRadio: function(field) {
        field.parent().find("span").removeClass("red");
        field.off("change");       
    },
    
    markRedText: function(field) {
        var m = this;
        field.addClass("red");
        field.siblings("input").addClass("red");
        field.on("keypress", function() { m.unmarkRedText($("[name='" + this.name + "']")); });
    },
    
    markRedRadio: function(field) {
        var m = this;
        field.parent().find("span").addClass("red")
        field.on("change", function() { m.unmarkRedRadio($("[name='" + this.name + "']")); });
    },
    
    checkFormatDate: function(fields) {
        var check = 0;
        for (var i = 0; i < fields.length; i++) {   
            var f = $("[name='" + fields[i] + "']");
            var v = f.val();
            this.unmarkRedText(f);
            if (v != "") {
                if (/^(\d{2}\.){2}\d{4}$/i.test(v)) {            
                    var p = v.split(".");        
                    var d = Date.parse(p[2] + "-" + p[1] + "-" + p[0]);   
                    if (isNaN(d)) {
                        this.markRedText(f);
                        check++;
                    }
                } else {
                    this.markRedText(f);
                    check++;
                }
            }
        }        
        return this.checkResultReturn(check, "Неправильный формат даты(используйте ДД.MM.ГГГГ).");
    },
    
    checkEqualPair: function(left, right) {
        var check = 0;
        var l = $("[name='" + left + "']");
        var r = $("[name='" + right + "']");
        this.unmarkRedText(l);
        this.unmarkRedText(r);
        if (l.val() != r.val()) {
            this.markRedText(l);
            this.markRedText(r);
            check++;
        }
        return this.checkResultReturn(check, "Значения полей \"" + l.prop("placeholder") + "\" и \"" + r.prop("placeholder") + "\" не совпадают!");
    },
    
    checkFormMandatory: function(fields) {
        var check = 0;
        for (var i = 0; i < fields.length; i++) {
            var f = $("[name='" + fields[i] + "']");
            if (f.prop("type").toLowerCase() != "radio") {
                this.unmarkRedText(f);
                if (f.val() == "") {
                    this.markRedText(f);
                    check++;
                }
            } else {
                this.unmarkRedRadio(f);
                if (!f.is(":checked")) {
                    this.markRedRadio(f);
                    check++;
                }
            }
        }
        return this.checkResultReturn(check, "Не все обязательные поля заполнены!");
    },
    
    sendFormData: function(form, fields) { 
        var a = form.prop("action");
        var m = this;
        
        if (History.enabled) {
            var d = {}; 
            for (var i = 0; i < fields.length; i++) {
                var f = $("[name='" + fields[i] + "']");
                if (f.prop("type").toLowerCase() == "radio") {
                   f = f.filter(":checked");
                }
                if (f.val() != "") {
                    d[fields[i]] = f.val();
                }
            }            
            console.log(JSON.stringify(d));            
            $.ajax({
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                mimeType: "application/json",
                data: JSON.stringify(d),
                method: "POST", 
                url: a
            }).done(function(data) {
                if (data.id >= 0) {
                    History.pushState(null, "RHHCC | Выполнено", m.url(data.path));
                } else {
                    m.showMessage("error", data.text);
                }
            }).fail(function(jqXHR, textStatus, errorThrown) {
                m.showMessage("error", jqXHR.status + ": " + errorThrown);
            });
        } else {
            form.prop("action", a+"/submit?_csrf=" + $.cookie("XSRF-TOKEN")).submit();
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