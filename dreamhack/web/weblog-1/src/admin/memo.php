<a href="javascript:history.back(-1);">Back</a><br/><br/>
<?php
  if($level[$_SESSION['level']] !== "admin") { die("Only Admin !"); }

  if(isset($_GET['memo'])){
    $_SESSION['memo'] = $_GET['memo'];
  }

  if(isset($_SESSION['memo'])){
    echo($_SESSION['memo']);
  }

?>

<form>
  <input type="hidden" name="page" value="memo.php">
  <div class="form-group">
    <label for="memo">memo</label>
    <input type="text" class="form-control" name="memo" id="memo" placeholder="memo">
  </div>
  <button type="submit" class="btn btn-default">Write</button>
</form>