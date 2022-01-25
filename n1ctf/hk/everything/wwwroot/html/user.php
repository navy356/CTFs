<?php
require_once("_lib.php");
class User{
    public $boxdir,$pass;
    public function __construct($b)
    {
        $this->boxdir=$b;
    }
    public function udir(){
        return BOXBASE."_user/".santize($this->boxdir,50).'/';
    }
    public function list(){
        return scandir($this->udir());
    }
    public function cpath($id){
        return $this->udir().santize($id,50);
    }    
    public function read($id){
        $p=$this->cpath($id);
        must(file_exists($p));
        return file_get_contents($p);
    }
    public function write($id,$cont){
        $p=$this->cpath($id);
        //printf("write to %s\n",$p);
        //must(file_exists($p));
        if(strlen($cont)>1024){
            die("too long");
        }
        file_put_contents($p,$cont);
    }
    public function del($id){
        $p=$this->cpath($id);
        must(file_exists($p));
        unlink($p);
    }
    public function __toString(){
        return print_r($this->list(),true);
    }
};
?>
