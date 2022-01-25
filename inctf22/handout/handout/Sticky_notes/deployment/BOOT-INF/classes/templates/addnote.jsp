<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Add notes</title>
  <link rel="stylesheet" href="./style1.css">

</head>
<body>
<center><h2>Add Notes</h2></center>
<!-- partial:index.partial.html -->
<!-- instagram:ogzozbn -->
<div class="center">
<div class="container">
<form action="/add" method="POST">
<table>
<tr>
	<td>
    <textarea required id="text-area" name="note" placeholder="note..."></textarea>
    </td>
    <td>
    <div class="emoji">
        <span>🙂</span>
        <div id="emoji-picker">
            <div class="emoji-arrow"></div>
        </div>
    </div>
    </td>
</tr>
<tr>
<td>
    <input type="submit" value="AddNote">
</td>
</tr>
</table>
</form>
</div>
</div>
<!-- partial -->
  <script src='https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js'></script><script  src="./script1.js"></script>

</body>
</html>