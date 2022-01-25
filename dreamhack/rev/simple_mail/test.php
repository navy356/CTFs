<?php


function generateRandomString($length = 10)
{
    $characters = '0123456789';
    $charactersLength = strlen($characters);
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $randomString .= $characters[rand(0, $charactersLength - 1)];
    }
    return $randomString;
}


while (1) {
    $t = '0e'.generateRandomString(11);
    echo $t.PHP_EOL;
    if (hash('md5', $t) == $t) {
        echo $t;
        break;
    }
    $t = "";
}
