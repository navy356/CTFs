<?php

if (isset($_GET['view-source'])) {
    show_source(__FILE__);
    exit();
}

/*
create table user(
 idx int auto_increment primary key,
 id char(32),
 ps char(32)
);
*/

if (isset($_POST['id']) && isset($_POST['ps'])) {
    include("./lib.php"); # include for $FLAG, $DB_username, $DB_password.

    $conn = mysqli_connect("localhost", $DB_username, $DB_password, "login_filtering");
    mysqli_query($conn, "set names utf8");

    $id = mysqli_real_escape_string($conn, trim($_POST['id']));
    $ps = mysqli_real_escape_string($conn, trim($_POST['ps']));

    $row = mysqli_fetch_array(mysqli_query($conn, "select * from user where id='$id' and ps=md5('$ps')"));
    if (isset($row['id'])) {
        if ($id == 'guest' || $id == 'blueh4g') {
            echo "your account is blocked";
        } else {
            echo "login ok" . "<br />";
            echo "FLAG : " . $FLAG;
        }
    } else {
        echo "wrong..";
    }
}
?>
<!DOCTYPE html>
<style>
    * {
        margin: 0;
        padding: 0;
    }

    body {
        background-color: #ddd;
    }

    #mdiv {
        width: 200px;
        text-align: center;
        margin: 50px auto;
    }

    input[type=text],
    input[type=[password] {
        width: 100px;
    }

    td {
        text-align: center;
    }
</style>

<body>
    <form method="post" action="./">
        <div id="mdiv">
            <table>
                <tr>
                    <td>ID</td>
                    <td><input type="text" name="id" /></td>
                </tr>
                <tr>
                    <td>PW</td>
                    <td><input type="password" name="ps" /></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit" value="login" /></td>
                </tr>
            </table>
            <div><a href='?view-source'>get source</a></div>
    </form>
    </div>
</body>
<!--

you have blocked accounts.

guest / guest
blueh4g / blueh4g1234ps

-->