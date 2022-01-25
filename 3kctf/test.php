<?php
$flag="yayyyyyyyy";
$page=$_GET['page'];
/*$a=file_get_contents("https://raw.githubusercontent.com/3kctf2021webchallenge/downloader/master/".$page.".html");
preg_match_all("/<img src=\"(.*?)\">/", $a,$ma);
var_dump($ma);
echo($a);*/
$d = "bash -c \"curl -o /dev/null ".escapeshellarg("https://raw.githubusercontent.com/navy356/downloader2/master/".$page)."  \"";
echo($d);
echo(escapeshellarg($page));
exec($d);
?>
<html>
<head>
    <title>a</title>
</head>
<body>
</body>