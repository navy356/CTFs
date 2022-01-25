<?php
if (($_SERVER["REQUEST_METHOD"] ?? 'GET') == 'POST'){
    if(isset($_POST["content"])){
       $file_name=rand(4060,9999).'.html';
        file_put_contents("files/".$file_name,$_POST["content"]);
        passthru("java -Xmx512m -Djava.awt.headless=true -cp /var/www/html/pd4ml_demo.jar Pd4Cmd file:////var/www/html/files/$file_name 800 A4 -out /var/www/html/files/result.pdf");
    }
    else{
	echo "Try harder!!, pal";
	}
}
else{
    echo "Try harder!!!, pal";
}
?>
