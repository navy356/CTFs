<?php
session_start();

function dbconn(){
  $conn = mysqli_connect(
    "localhost",
    "board",
    "password",
    "simple_board");
  return $conn;
}

function error($code){
  http_response_code($code);
}

$level = array(
	0 => "admin",
	1 => "guest"
);

$upload_dir = "./uploads/";
