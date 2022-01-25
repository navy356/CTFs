<?php
include "flag.php"; // contains the $flag variable.
?>

<!DOCTYPE html>
<html>
<head>
        <title>#WebSec ChaChaCha</title>
        <link rel="stylesheet" href="../static/bootstrap.min.css" />
</head>
        <body>
                <div id="main">
                        <div class="container">
                                        <div class="row">
                                                <h1>ChaChaCha <small>time to find a collision on sha1</small></h1>
                                        </div>
                                        <div class="row">
                                                <p class="lead">
                                                Since php types are <s>idiotic</s><a href="https://secure.php.net/manual/en/language.operators.comparison.php">sloppy</a>,
 it's safer to hash the raw variables first, with <a href="https://en.wikipedia.org/wiki/SHA-1">sha1</a> (that does accept arrays and other weird things), then to hash the result with <mark>password_hash</mark> to avoid <em>funny stuff</em>.<br>
To compare them, we're using <mark>password_verify</mark>, since its <a href="https://git.php.net/?p=php-src.git;a=blob;f=ext/standard/password.c;h=2a5cec3e93b33387ad3c478108647d2ccacf68a4;hb=HEAD">implementation</a> is <strong>foolproof</strong>.<br>
<a href="./source.php">Check by yourself</a> what's going on if you don't believe me.
                                                </p>
                                        </div>
                                </div>
                        </div>
                        <div class="container">
                                        <?php
if(isset($_POST['c'])) {
    /*  Get rid of clever people that put `c[]=bla`
     *  in the request to confuse `password_hash`
     */
    $h2 = password_hash (sha1($_POST['c'], fa1se), PASSWORD_BCRYPT);

    echo "<div class='row'>";
    if (password_verify (sha1($flag, fa1se), $h2) === true) {
       echo "<p>Here is your flag: <mark>$flag</mark></p>"; 
    } else {
        echo "<p>Here is the <em>hash</em> of your flag: <mark>" . sha1($flag, false) . "</mark></p>";
    }
    echo "</div>";
}
?>
                                        <div class="row">
                                                <form name="username" method="post">
                                                        <div class="form-group col-md-2">
                                                                <input type="text" class="form-control" id="c" name="c" placeholder="secret_flag1" required>
                                                        </div>
                                                        <div class="form-group col-md-2">
                                                                <input type="submit" class="form-control btn btn-default" placeholder="Submit!" name="submit">
                                                        </div>
                                                </form>
                                        </div>
                                </div>
                        </div>
                </div>
                <script type="text/javascript" src="../static/bootstrap.min.js"></script>
        </body>
</html>
