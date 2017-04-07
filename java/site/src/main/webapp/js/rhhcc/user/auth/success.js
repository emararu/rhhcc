$(document).ready(function() {
    window.opener.cjs.swapMenuAuth($("input[type=hidden]").val());
    window.close();
});