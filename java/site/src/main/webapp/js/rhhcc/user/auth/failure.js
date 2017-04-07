$(document).ready(function() {    
    window.opener.cjs.showMessage("error", $("input[type=hidden]").val());
    window.close();        
});