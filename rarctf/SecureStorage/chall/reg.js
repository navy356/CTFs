function register()
{
    var xhr=new XMLHttpRequest();
    xhr.open("POST",'/api/register',true);
    xhr.setRequestHeader("Content-Type", "application/json");
    var params = {"user" : {"layout": "./../routes/index.js"}, "pass":"test" };
    xhr.onload = function()
    {//Call a function when the state changes.
        if(xhr.status == 200)
        {
            console.log(this.responseText);
        }
    }
    xhr.send(params)
}