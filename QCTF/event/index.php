<?php

require_once("db.php");

$events = get_top_events();

require_once("header.php");
?>

<p>
  <ul>
<?php
foreach ($events as $event) {
  echo "<li>" . htmlentities($event['name']) . "</li>";
}
?>
  </ul>
</p>

<?php require_once("footer.php");
