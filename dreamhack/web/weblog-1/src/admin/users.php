<a href="javascript:history.back(-1);">Back</a><br/><br/>
<?php
  if($level[$_SESSION['level']] !== "admin") { die("Only Admin !"); }
  $conn = dbconn();
  $sql = "SELECT * FROM users ";
  if(isset($_GET['search'])){
    $search = mysqli_real_escape_string($conn, $_GET['search']);
    $sql .= "Where username like '%${search}%' ";
  }
  $result = mysqli_query($conn, $sql) or die(error(500));;
  while( $row = mysqli_fetch_assoc($result)){
      echo '<li>'.htmlentities($row['username']).' - '.htmlentities($row['level']).'</li>';
    }
?>