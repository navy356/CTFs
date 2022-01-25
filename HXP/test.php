<?php
$pattern = "(^|\\s+)\W+";
//$pattern = "(\\s+)\W+";
//$pattern="(^|a)bcde";
//$pattern = "(abc)de";
$string = "a ++++";
var_dump($string);
mb_ereg($pattern, $string, $matches);
//$test = mb_ereg_replace($pattern, '\\1', $string);
var_dump($matches);
//var_dump($test);
?>
