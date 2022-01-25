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
              include "./config.php";
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
      <a class="btn btn-success" href="/write.php" role="button">Write</a><br/><br/>
      <ul>
    <?php
      $conn = dbconn();
      $sql = "SELECT * FROM board ";
      if(isset($_GET['search'])){
        $search = mysqli_real_escape_string($conn, $_GET['search']);
        $sql .= "Where title like '%${search}%' ";
      }
      if(isset($_GET['sort']) && $_GET['sort'] != "" ){
        $sql .= "order by ". $_GET['sort']; 
      }else{
        $sql .= "order by idx asc";
      }
      $result = mysqli_query($conn, $sql) or die(error(500));
      while( $row = mysqli_fetch_assoc($result)){
          echo '<li>'.htmlentities($row['title']).' - '.htmlentities($row['writer']).'</li>'."<br/>";
          echo '<pre>'.htmlentities($row['contents']).'</pre>';
        }
      
    ?> 
    </ul></div> 
</body>
</html>