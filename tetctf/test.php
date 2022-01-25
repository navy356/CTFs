<?php
$wl = preg_match('/^[0-9\+\-\*\/\(\)\'\.\~\^\|\&]+$/i', $_GET["calc"]);
var_dump($_GET['calc']);
var_dump($wl);
if($wl === 0 || strlen($_GET["calc"]) > 70) {
        die("Tired of calculating? Lets <a href='https://www.youtube.com/watch?v=wDe_aCyf4aE' target=_blank >relax</a> <3");
    }
echo 'Result: ';
eval("echo ".eval("return ".$_GET["calc"].";").";");
?>
