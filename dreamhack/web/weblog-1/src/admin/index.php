<html>
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<title>Simple Board</title>
</head>
<body>
    <!-- Fixed navbar -->
    <nav class="navbar navbar-default navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <a class="navbar-brand" href="/">Simple Board</a>
        </div>
        <div id="navbar">
          <ul class="nav navbar-nav">
            <li><a href="/">Home</a></li>
            <li><a href="/board.php">Board</a></li>
            <?php 
              include "../config.php";
              if(isset($_SESSION['username'])){
            ?>
            <li><a href="/logout.php">Logout</a></li>
            <?php
            }else{
            ?>
            <li><a href="/login.php">Login</a></li>
            <?php              
            }
            ?>
          </ul>

        </div><!--/.nav-collapse -->
      </div>
    </nav><br/><br/><br/>
    <div class="container">
      <ul>
    <?php
      if($level[$_SESSION['level']] !== "admin") { die("Only Admin !"); }
      if(isset($_GET['page'])){
        include $_GET['page'];
      }else{
    ?>
      <li><a href="./?page=users.php">User List</a></li>
      <li><a href="./?page=memo.php">Session Memo</a></li>
    <?php
      }
    ?>
    </ul></div> 
</body>
</html>