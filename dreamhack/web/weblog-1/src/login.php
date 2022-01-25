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
      if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    ?>
      <form method="POST">
        <div class="form-group">
          <label for="username">username</label>
          <input type="text" class="form-control" name="username" id="username" placeholder="username">
        </div>
        <div class="form-group">
          <label for="passowrd">암호</label>
          <input type="password" class="form-control" name="password" id="password" placeholder="password">
        </div>
        <button type="submit" class="btn btn-default">Login</button>
      </form>
    <?php
      }elseif($_SERVER['REQUEST_METHOD'] === 'POST'){
        $conn = dbconn();
        if(isset($_POST['username']) && isset($_POST['password'])){
          $username = mysqli_real_escape_string($conn, $_POST['username']);
          $password = mysqli_real_escape_string($conn, $_POST['password']);
          $sql = "SELECT * FROM users WHERE username='${username}' and password='${password}'";
          $result = mysqli_query($conn, $sql) or die(error(500));
          $row = mysqli_fetch_array($result);
          if($row['username'] === $username && $row['password'] === $password ){
            $_SESSION['username'] = $row['username'];
            $_SESSION['level'] = $row['level'];
            die("<h2>Login Success !</h2>");
          }
        }
        die("<h2>Login Fail !</h2>");
      }
      
    ?> 
    </ul></div> 
</body>
</html>