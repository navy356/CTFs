<?php
include_once "../vendor/autoload.php";

error_reporting(0);
session_start();

define("UPLOAD_PATH", "/tmp/sandbox");
if (!file_exists(UPLOAD_PATH)) {
    @mkdir(UPLOAD_PATH);
}

function make_user_upload_dir() {
    $md5_dir = md5($_SERVER['REMOTE_ADDR'] . session_id());
    $upload_path = UPLOAD_PATH . "/" . $md5_dir;
    @mkdir($upload_path);
    $_SESSION["upload_path"] = $upload_path;
}

if (empty($_SESSION["upload_path"])) {
    make_user_upload_dir();
}

if (!empty($_FILES['file'])) {
    $file = $_FILES['file'];
    if ($file['size'] < 1024 * 1024) {
        if (!empty($_POST['path'])) {
            $upload_file_path = $_SESSION["upload_path"]."/".$_POST['path'];
            $upload_file = $upload_file_path."/".$file['name'];
        } else {
            $upload_file_path = $_SESSION["upload_path"];
            $upload_file = $_SESSION["upload_path"]."/".$file['name'];
        }

        if (move_uploaded_file($file['tmp_name'], $upload_file)) {
            echo "OK! Your file saved in: " . $upload_file;
        } else {
            echo "emm...Upload failed:(";
        }
    } else {
        echo "too big!!!";
    }
} else if (!empty($_GET['phpinfo'])) {
    phpinfo();
    exit();
} else {
    unserialize("O:60:\"Symfony\\Component\\HttpKernel\\DataCollector\\DumpDataCollector\":7:{s:7:\"*data\";a:3:{i:0;a:4:{s:4:\"data\";s:1:\"1\";s:4:\"name\";O:40:\"Symfony\\Component\\Form\\FormErrorIterator\":2:{s:4:\"form\";N;s:48:\"Symfony\\Component\\Form\\FormErrorIteratorerrors\";a:1:{i:0;O:40:\"Symfony\\Component\\Form\\FormErrorIterator\":2:{s:4:\"form\";O:41:\"Symfony\\Component\\Cache\\Traits\\RedisProxy\":2:{s:48:\"Symfony\\Component\\Cache\\Traits\\RedisProxyredis\";s:9:\"phpinfo()\";s:54:\"Symfony\\Component\\Cache\\Traits\\RedisProxyinitializer\";O:39:\"Symfony\\Component\\Console\\Helper\\Dumper\":1:{s:48:\"Symfony\\Component\\Console\\Helper\\Dumperhandler\";a:2:{i:0;O:44:\"Symfony\\Component\\Cache\\Adapter\\ProxyAdapter\":3:{s:61:\"Symfony\\Component\\Cache\\Adapter\\ProxyAdaptercreateCacheItem\";s:2:\"dd\";s:55:\"Symfony\\Component\\Cache\\Adapter\\ProxyAdapternamespace\";s:0:\"\";s:50:\"Symfony\\Component\\Cache\\Adapter\\ProxyAdapterpool\";O:43:\"Symfony\\Component\\Cache\\Adapter\\NullAdapter\":1:{s:60:\"Symfony\\Component\\Cache\\Adapter\\NullAdaptercreateCacheItem\";s:6:\"assert\";}}i:1;s:7:\"getItem\";}}}s:48:\"Symfony\\Component\\Form\\FormErrorIteratorerrors\";a:0:{}}}}s:4:\"file\";s:1:\"3\";s:4:\"line\";s:1:\"4\";}i:1;N;i:2;N;}s:71:\"Symfony\\Component\\HttpKernel\\DataCollector\\DumpDataCollectorstopwatch\";N;s:76:\"Symfony\\Component\\HttpKernel\\DataCollector\\DumpDataCollectorfileLinkFormat\";N;s:71:\"Symfony\\Component\\HttpKernel\\DataCollector\\DumpDataCollectordataCount\";i:0;s:73:\"Symfony\\Component\\HttpKernel\\DataCollector\\DumpDataCollectorisCollected\";b:0;s:73:\"Symfony\\Component\\HttpKernel\\DataCollector\\DumpDataCollectorclonesCount\";i:0;s:73:\"Symfony\\Component\\HttpKernel\\DataCollector\\DumpDataCollectorclonesIndex\";i:0;}");
    echo <<<CODE
<html>
    <head>
        <title>Upload</title>
    </head>

    <body>
        <h1>Upload files casually XD</h1>
        <form action="index.php" method="post" enctype="multipart/form-data">
            FILE: <input type="file" name="file">
            PATH: <input type="text" name="path">
            <input type="submit">
        </form>

        <hr>

        <h3>or...Just look at the phpinfo?</h3>
        <a href="./index.php?phpinfo=1">go to phpinfo</a>
    </body>
</html>
CODE;
}