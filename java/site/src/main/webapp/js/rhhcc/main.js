$(function() {

    if (History.enabled) {        
        History.Adapter.bind(window, "statechange", function() { 
            var State = History.getState();
            $.ajax({
                dataType: "html", 
                method: "POST",
                url: State.url
            }).done(function(data) {
                if (State.url.indexOf(url("/user/auth/tile-menu-auth")) > -1) {
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
            cjs.hideMenuAuth();    
        });         
    }
    
    $(document).ajaxSend(function(e, xhr, options) {
        var header = "X-XSRF-TOKEN";
        var token  = $.cookie("XSRF-TOKEN"); 
        xhr.setRequestHeader(header, token);
    });
    
    $(window).resize(function() {
        cjs.hideMenuAuth();    
    });    
    
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
        cjs.windowOpen(cjs.url("/user/auth/facebook/login"), 750, 650);
        cjs.hideMenuAuth();
    });
        
    $("#menu-main-auth").on("click", "#menu-main-auth-google", function() {
        cjs.windowOpen(cjs.url("/user/auth/google/login"), 750, 650);
        cjs.hideMenuAuth();
    });     
        
    $("#menu-main-auth").on("click", "#menu-main-auth-logout", function() {     
        $.ajax({
            dataType: "html", 
            method: "POST",
            url: cjs.url("/logout")
        }).done(function() {
            cjs.swapMenuAuth("Пока!");
        }).fail(function(jqXHR, textStatus, errorThrown) {
            cjs.showMessage("error", jqXHR.status + ": " + errorThrown);
        });  
        cjs.hideMenuAuth();
    });
            
    $("[placeholder]").placeholder();
});