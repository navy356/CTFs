<?php

$json = new stdClass();
$json->error = true;

if (isset($_GET["action"])) {
    if (isset($_GET["name"]) && isset($_GET["password"])) {

        $account = "./" . $_GET["name"] . ".json";
        $file = file_get_contents($account);

        if ($_POST["action"] === "activate") {

            $file = str_replace("false", "true", $file);

            $json->error = false;
            $json->text = "activated";

            sleep(5); // brute force protection
        }
        if ($_GET["action"] === "signup") {

            $newjson = new stdClass();
            $newjson->password = $_GET["password"];
            $newjson->enable = false;

            file_put_contents($account, json_encode($newjson));

            $json->error = false;
            $json->text = "new created";
        }
        if ($_GET["action"] === "read") {

            if (file_exists($account)) {
                $newjson = json_decode($file);
            }

            $json->error = false;
            $json->text = $newjson->data;
        }
        if ($_GET["action"] === "write") {
            if (file_exists($account)) {
                $newjson = json_decode(file_get_contents($account));
            }
		var_dump($newjson->password);
		var_dump($_GET["password"]);

            if ($_GET["password"] === $newjson->password) {

                $newjson->data = $_GET["data"];
		var_dump($newjson);

                if (file_exists($account))
                    file_put_contents($account, json_decode($newjson));

                sleep(5); // make sure the write was successful

                if (!file_exists($account))
                    file_put_contents($account, $file);

                $json->error = false;
                $json->text = "ok";
            } else {
                $json->text = "password wrong";
            }
        }
    } else {
        $json->text = "no name or pass";
    }
} else {
    $json->text = "no action";
}

echo json_encode($json);
