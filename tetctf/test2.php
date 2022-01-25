<?php
$db = mysqli_connect("localhost","navy","Ab12cd34!#","misc") or die($db);
var_dump(mysqli_real_escape_string($db,"1 unio/**/n select version()"));
?>
