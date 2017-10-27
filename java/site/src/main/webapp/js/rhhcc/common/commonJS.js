function CommonJS(path) {
    var re = new RegExp("/", "g");
    this.path = "/" + path.replace(re, "");    
}
    
CommonJS.prototype.url = function(path) {
    return this.path + path;
};
    
CommonJS.prototype.windowOpen = function(url, height, width) {
    window.open(url, '', 'width='+width+',height='+height+',left='+((window.innerWidth-width)/2)+',top='+((window.innerHeight-height)/2)+',menubar=no,toolbar=no,location=no');
};
        
CommonJS.prototype.showMessage = function(style, text) {
    if (text != "") {
        var message = $("#message");
        message.removeClass("error ok").addClass(style);
        message.show();
        message.html(text); 
        setTimeout(function(){ message.fadeOut('fast'); }, 5000);    
    }              
};
    
CommonJS.prototype.swapMenuAuth = function(text) {   
    var _this = this;
    $.ajax({
        dataType: "html", 
        method: "POST",
        url: _this.url("/user/auth/tile-icon-auth")
    }).done(function(icon) {
        $("#menu-main").find("#menu-main-auth-item").replaceWith(icon);
    }).fail(function(jqXHR, textStatus, errorThrown) {
        _this.showMessage("error", jqXHR.status + ": " + errorThrown);
    }).then(
        function() {
            $.ajax({
                dataType: "html", 
                method: "POST", 
                url: _this.url("/user/auth/tile-menu-auth")
            }).done(function(menu) {
                $("#menu-box").find("#menu-main-auth").replaceWith(menu); 
                $("#menu-box").find("input").placeholder();
                _this.showMessage("ok", text);
            }).fail(function(jqXHR, textStatus, errorThrown) {
                _this.showMessage("error", jqXHR.status + ": " + errorThrown);
            });
        },
        function() {
        }
    );    
};
    
CommonJS.prototype.hideMenuAuth = function () {
    $("#menu-box").find("#menu-main-auth-item").removeClass("selected");
    $("#menu-box").find("#menu-main-auth").hide();
};
    
CommonJS.prototype.checkResultReturn = function(check, text) {        
    if (check == 0) {
        return true;
    } else {
        this.showMessage("error", text);
        return false;
    }
};
    
CommonJS.prototype.unmarkRedText = function(field) {
    field.removeClass("red");
    field.siblings("input").removeClass("red");
    field.off("keypress");        
};
    
CommonJS.prototype.unmarkRedRadio = function(field) {
    field.parent().find("span").removeClass("red");
    field.off("change");       
};
    
CommonJS.prototype.markRedText = function(field) { 
    var _this = this;
    field.addClass("red");
    field.siblings("input").addClass("red");
    field.on("keypress", function() { _this.unmarkRedText($("[name='" + this.name + "']")); });
};
    
CommonJS.prototype.markRedRadio = function(field) { 
    var _this = this;
    field.parent().find("span").addClass("red")
    field.on("change", function() { _this.unmarkRedRadio($("[name='" + this.name + "']")); });
};
    
CommonJS.prototype.checkFormatDate = function(form, fields) {
    var check = 0;
    for (var i = 0; i < fields.length; i++) {   
        var f = $(form).find("[name='" + fields[i] + "']");
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
};
    
CommonJS.prototype.checkEqualPair = function(form, left, right) {
    var check = 0;
    var l = $(form).find("[name='" + left + "']");
    var r = $(form).find("[name='" + right + "']");
    this.unmarkRedText(l);
    this.unmarkRedText(r);
    if (l.val() != r.val()) {
        this.markRedText(l);
        this.markRedText(r);
        check++;
    }
    return this.checkResultReturn(check, "Значения полей \"" + l.prop("placeholder") + "\" и \"" + r.prop("placeholder") + "\" не совпадают!");
};
    
CommonJS.prototype.checkFormMandatory = function(form, fields) {
    var check = 0;
    for (var i = 0; i < fields.length; i++) {
        var f = $(form).find("[name='" + fields[i] + "']");
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
};
    
CommonJS.prototype.sendForm = function(form, fields, callback) {   
    var _this = this;         
    var d = {}; 
    for (var i = 0; i < fields.length; i++) {
        var f = $(form).find("[name='" + fields[i] + "']");
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
        url: form.prop("action")
    }).done(function(data) {
        callback(data);
    }).fail(function(jqXHR, textStatus, errorThrown) {
        _this.showMessage("error", jqXHR.status + ": " + errorThrown);
    });
};
    
CommonJS.prototype.sendFormData = function(form, fields) { 
    var _this = this;
    if (History.enabled) {
        _this.sendForm(form, fields, function(data){
            if (data.id >= 0) {
                History.pushState(null, "RHHCC | Выполнено", _this.url(data.path));
            } else {
                _this.showMessage("error", data.text);
            }
        });
    } else {
        form.prop("action", form.prop("action")+"/submit?_csrf=" + $.cookie("XSRF-TOKEN")).submit();
    }
};