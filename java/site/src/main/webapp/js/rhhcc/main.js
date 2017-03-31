$(function() {

    if (History.enabled) {        
        History.Adapter.bind(window, "statechange", function() { 
            var State = History.getState();
            $.ajax({
                dataType: "html", 
                method: "POST",
                url: State.url
            }).done(function(data) {
                if (State.url.indexOf("${url_index}") > -1) {
                    $("#header").removeClass("header").addClass("header-tall");
                } else {                    
                    $("#header").removeClass("header-tall").addClass("header");
                }     
                $("div.main").scrollTop(0);
                $("#content").html(data);
            });
        });
        $("a").click(function(event) {
            event.preventDefault();           
            History.pushState(null, $(this).text(), $(this).attr("href"));
            hideMenuAuth();    
        });         
    }
    
    $(document).ajaxSend(function(e, xhr, options) {
        var header = "X-XSRF-TOKEN";
        var token  = $.cookie("XSRF-TOKEN"); 
        xhr.setRequestHeader(header, token);
    });
    
    $(window).resize(function() {
        hideMenuAuth();    
    });
});

function windowOpen(url, height, width) {
    window.open(url, '', 'width='+width+',height='+height+',left='+((window.innerWidth-width)/2)+',top='+((window.innerHeight-height)/2)+',menubar=no,toolbar=no,location=no');
};

function showMessage(style, text) {    
    if (text != "") {
        var message = $("#message");
        message.removeClass("error ok").addClass(style);
        message.show();
        message.html(text); 
        setTimeout(function(){ message.fadeOut('fast'); }, 2000);    
    }              
}

function swapMenuAuth(text) {    
    $.ajax({
        dataType: "html", 
        method: "POST",
        url: "${url_menu_auth_icon}"
    }).done(function(icon) {
        $("#menu-main-auth-item").html(icon);
    }).fail(function(jqXHR, textStatus, errorThrown) {
        showMessage("error", jqXHR.status + ": " + errorThrown);
    }).then(
        function() {
            $.ajax({
                dataType: "html", 
                method: "POST", 
                url: "${url_menu_auth}"
            }).done(function(menu) {
                $("#menu-main-auth").html(menu); 
                showMessage("ok", text);
            }).fail(function(jqXHR, textStatus, errorThrown) {
                showMessage("error", jqXHR.status + ": " + errorThrown);
            });
        },
        function() {
        }
    );    
}

function hideMenuAuth() {
    $("#menu-main-auth-item").removeClass("selected");
    $("#menu-main-auth").hide();
}

$(document).ready(function() {
    
    $("#message").click(function(){ 
        $(this).hide();
    });
    
    $("#menu-main-auth-item").click(function(){ 
        var top = $("#menu-box").outerHeight();
        var left = $("#menu-box").outerWidth()-$("#menu-main-auth").outerWidth(true);//-($("#menu-box").outerWidth()-$("#menu-main").outerWidth())/2;
        
        $("#menu-main-auth").css({"top": top, "left": left});
        $("#menu-main-auth").toggle();                
        $(this).toggleClass("selected");
    });
    
    $("#menu-main-auth").on("click", "#menu-main-auth-fb", function() {
        windowOpen("${url_login_facebook}", 750, 650);
        hideMenuAuth();
    });
        
    $("#menu-main-auth").on("click", "#menu-main-auth-google", function() {
        windowOpen("${url_login_google}", 750, 650);
        hideMenuAuth();
    });     
        
    $("#menu-main-auth").on("click", "#menu-main-auth-logout", function() {     
        $.ajax({
            dataType: "html", 
            method: "POST",
            url: "${url_logout}"
        }).done(function() {
            swapMenuAuth("Пока!");
        }).fail(function(jqXHR, textStatus, errorThrown) {
            showMessage("error", jqXHR.status + ": " + errorThrown);
        });  
        hideMenuAuth();
    });
    
});