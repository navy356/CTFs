<?php

$arghes = $_GET['args'];
print_r($arghes);
for ( $i=0; $i<count($arghes); $i++ ){
    if ( !preg_match('/^\w+$/', $arghes[$i]) )
        exit();
}
print_r($arghes);
exec("echo " . implode(" ", $arghes));

?>