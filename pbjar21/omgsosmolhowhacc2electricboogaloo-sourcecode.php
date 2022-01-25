<?php
$arghes = $_GET['args'];
for ( $i=0; $i<count($arghes); $i++ ){
    if (!preg_match('/^[\w-]+$/', $arghes[$i]))
    { 
        exit();
    }
    if(str_contains($arghes[$i], '-d'))
    {
        if(!str_contains($arghes[$i], '--') || str_contains($arghes[$i], 'data'))
        {
            exit();
        }
    }
}
exec("echo " . implode(" ", $arghes));
?>