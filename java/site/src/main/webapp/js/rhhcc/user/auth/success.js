$(document).ready(function() {
    window.opener.swapMenuAuth($("input[type=hidden]").val());
    window.close();
});