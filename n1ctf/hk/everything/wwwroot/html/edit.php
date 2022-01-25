<?php
require_once("user.php");
$srcusr=BOXBASE."_users/".GETK('user');
must(file_exists($srcusr));
$usr=unserialize(file_get_contents($srcusr));
must(isset($_GET['a']));
switch($_GET['a']){
        case 'sub':
            cooldown();
            must(isset($_POST['name'],$_POST['buf']));
            $usr->write($_POST['name'],$_POST['buf']);
        break;
        case 'new':
        case 'edit':
            render("edit.html",array("wpname"=>$_GET['a']=='new'?'new':$_GET['b'],"wpcont"=>$_GET['a']=='new'?'new':$usr->read($_GET['b'])));
            return;
            break;
        case 'del':
            cooldown();
            must(isset($_GET['b']));
            $usr->del($_GET['b']);
            break;
}
echo 'success <a href="list.php">back</a>';
?>