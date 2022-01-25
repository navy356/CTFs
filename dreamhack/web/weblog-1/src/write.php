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
      <ul>
    <?php
      if(!isset($_SESSION['username'])) die("<h3>Login First.</h3>");
      if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    ?>
      <form method="POST">
        <div class="form-group">
          <label for="title">title</label>
          <input type="text" class="form-control" name="title" id="title" placeholder="title">
        </div>
        <div class="form-group">
          <label for="contents">contents</label>
          <input type="text" class="form-control" name="contents" id="contents" placeholder="contents">
        </div>
        <button type="submit" class="btn btn-default">Write</button>
      </form>
    <?php
      }elseif($_SERVER['REQUEST_METHOD'] === 'POST'){
        $conn = dbconn();
        if(isset($_POST['title']) && isset($_POST['contents'])){
          $title = mysqli_real_escape_string($conn, $_POST['title']);
          $contents = mysqli_real_escape_string($conn, $_POST['contents']);
          $username = mysqli_real_escape_string($conn, $_SESSION['username']);
          $sql = "INSERT INTO board(title, contents, writer) values('${title}', '${contents}', '${username}')";
          $result = mysqli_query($conn, $sql) or die(error(500));
          if($result){
            die("Write Success !");
          }
        }
        die("Write Fail !");
      }
      
    ?> 

    </ul></div> 
</body>
</html>