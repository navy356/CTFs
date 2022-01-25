<?php
require_once("user.php");
cooldown();
$srcusr='';
if(isset($_GET['a'])){
    //has from user
    $srcusr=BOXBASE."_users/".md5($_GET['a']);
}else{
    //from me
    $srcusr=BOXBASE."_users/".GETK('user');
}
must(file_exists($srcusr));
$usr=unserialize(file_get_contents($srcusr));

render("detail.html",array("wpcont"=>$usr->read($_GET['b']),"wpname"=>$_GET['b']));
?>
