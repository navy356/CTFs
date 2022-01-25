<?php
ini_set('display_errors', 'On');
ini_set('html_errors', 0);

// ----------------------------------------------------------------------------------------------------
// - Error Reporting
// ----------------------------------------------------------------------------------------------------
error_reporting(-1);

// flag's in flag.php
if (isset($_GET['x'])) {
    $x = $_GET['x'];

    if (preg_match('/[A-Za-z0-9]/', $x))
        die("no alphanumeric");
    if (preg_match('/\$|=/', $x))
        die("no php");
    if (strlen($x) >= 58)
        die("no - ".strlen($x));
    var_dump($x);
    // yes
    echo eval($x);
} else {
    highlight_file(__FILE__);
}
