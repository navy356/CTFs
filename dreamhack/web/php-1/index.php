<html>
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<title>PHP Back Office</title>
</head>
<body>
    <!-- Fixed navbar -->
    <nav class="navbar navbar-default navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <a class="navbar-brand" href="/">PHP Back Office</a>
        </div>
        <div id="navbar">
          <ul class="nav navbar-nav">
            <li><a href="/">Home</a></li>
            <li><a href="/?page=list">List</a></li>
            <li><a href="/?page=view">View</a></li>
          </ul>

        </div><!--/.nav-collapse -->
      </div>
    </nav><br/><br/>
    <div class="container">
      <?php
          include $_GET['page']?$_GET['page'].'.php':'main.php';
      ?>
    </div> 
</body>
</html>