<?php
require_once("user.php");
cooldown();
echo 'view.php?a='.urlencode(GETK('uname')).'&b='.$_GET['b'];
?>