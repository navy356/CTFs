<?php
include 'connect.php';

$sql = new SQL();
$sql->connect();
$sql->query = 'SELECT username FROM users WHERE id=';


if (isset ($_COOKIE['leet_hax0r'])) {
    $sess_data = unserialize (base64_decode ($_COOKIE['leet_hax0r']));
    try {
        if (is_array($sess_data) && $sess_data['ip'] != $_SERVER['REMOTE_ADDR']) {
            die('CANT HACK US!!!');
        }
    } catch(Exception $e) {
        echo $e;
    }
} else {
    $cookie = base64_encode (serialize (array ( 'ip' => $_SERVER['REMOTE_ADDR']))) ;
    setcookie ('leet_hax0r', $cookie, time () + (86400 * 30));
}

if (isset ($_REQUEST['id']) && is_numeric ($_REQUEST['id'])) {
    try {
        $sql->query .= $_REQUEST['id'];
    } catch(Exception $e) {
        echo ' Invalid query';
    }
}
?>

<!DOCTYPE html>
<html>
<head>
        <title>#WebSec Level Four</title>
        <link rel="stylesheet" href="../static/bootstrap.min.css" />
</head>
        <body>
                <div id="main">
                        <div class="container">
                                <div class="row">
                                        <h1>LevelFour <small> - Cereal is nation</small></h1>
                                </div>
                                <div class="row">
                                        <p class="lead">
                        Since we're lazy, we take advantage of php's garbage collector to properly display query results.<br>
                         We also do like to write neat OOP.
                        You can get the sources <a href="source1.php">here</a> and <a href="source2.php">here</a>.
                                        </p>
                                </div>
                        </div>
                        <div class="container">
                <div class="row">
                    <form class="form-inline" method='post'>
                        <input name='id' class='form-control' type='text' placeholder='User id'>
                        <input class="form-control btn btn-default" name="submit" value='Go' type='submit'>
                                        </form>
                                </div>
                        </div>
                </div>
        </body>
</html>