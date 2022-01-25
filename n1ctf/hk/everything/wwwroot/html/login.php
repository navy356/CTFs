<?php
require_once("user.php");
cooldown();
$name=$_POST['name'];
$pass=$_POST['pass'];
must(strlen($name)<=16);
$upath=BOXBASE."_users/".md5($name);
if(file_exists($upath)){
    //login
    $usr=unserialize(file_get_contents($upath));
    if($usr->pass!==md5($pass)){
        die("login failed");
    }
}else{
    //reg
    cooldown2();
    $usr=new User(md5($name));
    $usr->pass=md5($pass);
    file_put_contents($upath,serialize($usr));
    mkdir(BOXBASE."_user/".md5($name));
}
PUTK('user',md5($name));
PUTK('uname',$name);
header("Location: list.php");
