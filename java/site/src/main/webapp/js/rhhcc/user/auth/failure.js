$(document).ready(function() {    
    window.opener.showMessage("error", $("input[type=hidden]").val());
    window.close();        
});