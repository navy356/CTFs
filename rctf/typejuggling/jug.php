<?php
    if(isset($_GET['source'])) {
        highlight_file(__FILE__);
        die();
    } else {
        $value = "240610708";
        if (isset($_GET['hash'])) {
            if ($_GET['hash'] === $value) {
                die('It is not THAT easy!');
            } 
            $hash = md5($_GET['hash']);
            echo("hash=".$hash."\n");
            $key = md5($value);
            echo("key=".$key."\n");
            if($hash == $key) {
                include('flag.php');
                print "Congratulations! Your flag is: $flag";
            } else {
                print "Flag not found!";
            }
        } 
    }
?>