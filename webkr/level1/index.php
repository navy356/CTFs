<?php 
class LevelOne {
    public function doQuery($injection) {
        $pdo = new SQLite3('database.db', SQLITE3_OPEN_READONLY);
        
        $query = 'SELECT id,username FROM users WHERE id=' . $injection . ' LIMIT 1';
        
        $getUsers = $pdo->query($query);
        $users = $getUsers->fetchArray(SQLITE3_ASSOC);

        if ($users) {
            return $users;
        }

        return false;
    }
}

if (isset ($_POST['submit']) && isset ($_POST['user_id'])) {

    $lo = new LevelOne ();
    $userDetails = $lo->doQuery ($_POST['user_id']);
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>#WebSec Level One</title>
    <link rel="stylesheet" href="../static/bootstrap.min.css" />
</head>
    <body>
        <div id="main">
            <div class="container">
                <div class="row">
                    <h1>LevelOne <small> - Select the user by ID you wish to view</small></h1>
                </div>
                <div class="row">
                    <p class="lead">
                        <a href="source.php">This application</a> is used to view the username by the given user ID,
                        it will return the corresponding username from the database.<br>
                    </p>
                </div>
            </div>
            <div class="container">
                <?php if (isset ($userDetails) && !empty ($userDetails)): ?>
                    <div class="row">
                        <p class="well"><strong>Username for given ID</strong>: <?php echo $userDetails['username']; ?> </p>
                        <p class="well"><strong>Other User Details</strong>: <br />
                            <?php 
                            $keys = array_keys ($userDetails);
                            $i = 0;

                            foreach ($userDetails as $user) { 
                                echo $keys[$i++] . ' -> ' . $user . "<br />";
                            } 
                            ?> 
                        </p>
                    </div>
                <?php endif; ?>

                <div class="row">
                    <label for="user_id">Enter the user ID:</label>
                    <form name="username" method="post">
                        <div class="form-group col-md-2">
                            <input type="text" class="form-control" id="user_id" name="user_id" value="1" required>
                        </div>
                        <div class="col-md-2">
                            <input type="submit" class="form-control btn btn-default" placeholder="Submit!" name="submit">
                        </div>
                        <input type="hidden" id="token" name="token" value="<?php echo $_SESSION['token']; ?>">
                    </form>
                </div>
            </div>
        </div>
        <script type="text/javascript" src="../static/bootstrap.min.js"></script>
    </body>
</html>