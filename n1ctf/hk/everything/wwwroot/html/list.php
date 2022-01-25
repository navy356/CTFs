<?php
require_once("user.php");
$srcusr=BOXBASE."_users/".GETK('user');
must(file_exists($srcusr));
$usr=unserialize(file_get_contents($srcusr));
//print_r($usr->list());
$subsrc='<tr><td><a href="view.php?b={id}">{id}</a></td><td><a href="edit.php?a=del&b={id}">delete</a></td><td><a href="edit.php?a=edit&b={id}">edit</a></td></tr>';
$fin='';
foreach ($usr->list() as $value) {
    if($value!='.' and $value!='..'){
        must(is_file($usr->cpath($value)));
        $sub=str_replace("{id}",$value,$subsrc);
        $fin.=$sub;
    }
}
render("show.html",array("content"=>$fin));