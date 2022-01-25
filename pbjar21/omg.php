<?php
$arghes = $_GET['args'];
print('hey');
for ( $i=0; $i<count($arghes); $i++ ){
    print($arghes[$i]);
    if (!preg_match('/^[\w-]+$/', $arghes[$i]))
    { 
        print('nope1');
        exit();
    }
    if(str_contains($arghes[$i], '-d'))
    {
        print('nope2');
        if(!str_contains($arghes[$i], '--') || str_contains($arghes[$i], 'data'))
        {
            exit();
        }
    }
}
print('heyyy');
exec("echo " . implode(" ", $arghes));
?>