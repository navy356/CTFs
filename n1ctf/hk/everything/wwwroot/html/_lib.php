<?php
define("BOXBASE","/tmp/box/");
define("SECRET_KEY",RAND_SKEY);
//function pstr2ffi(string $str)
//function creatbuf($size)
//function releasestr($str)
//function getstr($str)
function getIP():string{
    return $_SERVER['REMOTE_ADDR'];
}
function cooldown(){
    $p=BOXBASE."_cooldown/".md5(getIP());
    if(!file_exists($p)){
        file_put_contents($p,time());
    }else{
        $x=intval(file_get_contents($p));
        if(time()-$x<2){
            die("operation too fast(cooldown 2s)");
        }
        file_put_contents($p,time());
    }
}
function cooldown2(){
    $p=BOXBASE."_cooldown2/".md5(getIP());
    if(!file_exists($p)){
        file_put_contents($p,time());
    }else{
        $x=intval(file_get_contents($p));
        if(time()-$x<60){
            die("operation too fast(cooldown 60s)");
        }
        file_put_contents($p,time());
    }
}
function render($path,array $p){
    $c=file_get_contents($path);
    foreach($p as $k=>$v){
        $c=str_replace("{".$k."}",$v,$c);
    }
    echo $c;
}
function pad(string $str){
    $plen=8-(strlen($str)%8);
    if($plen==0){$plen=8;}
    return $str.str_repeat(chr($plen),$plen);
}
function unpad(string $str){
    return substr($str,0,strlen($str)-ord($str[strlen($str)-1]));
}

function decrypt($cont,$key):string{
    $obj=pstr2ffi($cont);
    $out=creatbuf(strlen($cont));
    decrypt_impl($obj,strlen($cont)/8,$key,$out);
    $ret=getstr($out,strlen($cont));
    releasestr($obj);
    releasestr($out);
    return unpad($ret);
}
function encrypt($cont,$key):string{
    $cont=pad($cont);
    $obj=pstr2ffi($cont);
    $out=creatbuf(strlen($cont));
    encrypt_impl($obj,strlen($cont)/8,$key,$out);
    $ret=getstr($out,strlen($cont));
    releasestr($obj);
    releasestr($out);
    return $ret;
}
/*
function decrypt($a,$b){
    return $a;
}
function encrypt($a,$b){
    return $a;
}*/
function must($repr){
    if($repr==false) die('fail');
}
function GETK($key){
    if(!isset($_COOKIE[$key])){
        return false;
    }
    $v=decrypt(base64_decode($_COOKIE[$key]),SECRET_KEY);
    if(($v1=unserialize($v))!=false){
        return $v1;
    }
    return $v;
}
function PUTK($key,$val){
    $val=serialize($val);
    setcookie($key,base64_encode(encrypt($val,SECRET_KEY)));
}
function santize($v,$maxlen=999){
    if(strlen($v)>$maxlen) die("sanitize failed");
    if(strspn($v,"0123456789abcdefghijklmnopqrstuvwxyz")!=strlen($v)){
        die("sanitize failed");
    }
    return $v;
}
?>
