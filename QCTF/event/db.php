<?php

/*
CREATE TABLE events (
  name char(64),
  secret char(64),
  description varchar(250)
);

INSERT INTO events VALUES('facebook', sha256(....), 'FLAG_HERE');
INSERT INTO events VALUES('messenger', sha256(....), ....);
INSERT INTO events VALUES('instagram', sha256(....), ....);
INSERT INTO events VALUES('whatsapp', sha256(....), ....);
INSERT INTO events VALUES('oculus-rift', sha256(....), ....);
*/
error_reporting(0);
require_once("config.php"); // DB config

$db = new mysqli($MYSQL_HOST, $MYSQL_USERNAME, $MYSQL_PASSWORD, $MYSQL_DBNAME);

if ($db->connect_error) {
  die("Connection failed: " . $db->connect_error);
}

function check_errors($var) {
  if ($var === false) {
    die("Error. Please contact administrator.");
  }
}

function get_top_events() {
  global $db;
  $statement = $db->prepare(
    "SELECT name FROM events LIMIT 5"
  );
  check_errors($statement);
  check_errors($statement->execute());
  $res = $statement->get_result();
  check_errors($res);
  $events = [];
  while ( ($event = $res->fetch_assoc()) !== null) {
    array_push($events, $event);
  }
  $statement->close();
  return $events;
}

function get_event($name) {
  global $db;
  $statement = $db->prepare(
    "SELECT name, description FROM events WHERE name = ?"
  );
  check_errors($statement);
  $statement->bind_param("s", $name);
  check_errors($statement->execute());
  $res = $statement->get_result();
  check_errors($res);
  $event = $res->fetch_assoc();
  $statement->close();
  return $event;
}

function insert_event($name, $secret, $description) {
  global $db;
  $statement = $db->prepare(
    "INSERT INTO events (name, secret, description) VALUES
      (?, ?, ?)"
  );
  check_errors($statement);
  $statement->bind_param("sss", $name, $secret, $description);
  check_errors($statement->execute());
  $statement->close();
}

function check_name_secret($name, $secret) {
  global $db;
  $valid = false;
  $statement = $db->prepare(
    "SELECT name FROM events WHERE name = ? AND secret = ?"
  );
  check_errors($statement);
  $statement->bind_param("ss", $name, $secret);
  check_errors($statement->execute());
  $res = $statement->get_result();
  check_errors($res);
  if ($res->fetch_assoc() !== null) {
    $valid = true;
  }
  $statement->close();
  return $valid;
}
