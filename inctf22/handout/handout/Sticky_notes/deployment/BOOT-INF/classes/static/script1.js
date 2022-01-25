/* https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js */

var emojiPicker = function () {
    var i = null;
    var emojiCode = [
/*
* For emoji codes
* https://www.w3schools.com/charsets/ref_emoji.asp 
*/
            128526,
            128565,
            129299,
            129322,
            128516,
        ];

    for (i = 0; i < emojiCode.length; i++) {
        $("#emoji-picker").append("<span class='my-emoji'>" + "&#" + emojiCode[i] + "</span>");
    }

    $(document).on("click", ".my-emoji", function () {
        var textArea = $('#text-area');
        textArea.val(textArea.val() + $(this).text());
        $("#emoji-picker").hide();
        textArea.focus();
    });
}

emojiPicker();

/* Toogle */
$(".emoji").click(function (e) {
    e.preventDefault();
    $("#emoji-picker").toggle();
});