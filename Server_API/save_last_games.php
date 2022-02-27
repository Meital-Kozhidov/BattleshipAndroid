<?php
 
/*Updates username's row in last_games table with the result of the last game he played. The order is from most recent to last*/

$username=$_POST['username'];
$result=$_POST['result'];

//array of responses
$output=array();
 
//require database
require_once('db.php');


$conn=$dbh->prepare("SELECT * FROM last_games WHERE username= ?");
$conn->bindParam(1,$username);
$conn->execute();
$results=$conn->fetch(PDO::FETCH_OBJ);
$game10=$results->game9;
$game9=$results->game8;
$game8=$results->game7;
$game7=$results->game6;
$game6=$results->game5;
$game5=$results->game4;
$game4=$results->game3;
$game3=$results->game2;
$game2=$results->game1;

$con=$dbh->prepare('UPDATE last_games SET game1=?,game2=?,game3=?,game4=?,game5=?,game6=?,game7=?,game8=?,game9=?,game10=? WHERE username= ?');
	$con->bindParam(11,$username);
	$con->bindParam(1,$result);
	$con->bindParam(2,$game2);
	$con->bindParam(3,$game3);
	$con->bindParam(4,$game4);
	$con->bindParam(5,$game5);
	$con->bindParam(6,$game6);
	$con->bindParam(7,$game7);
	$con->bindParam(8,$game8);
	$con->bindParam(9,$game9);
	$con->bindParam(10,$game10);
	$con->execute();


if($con->rowCount() == 0){
	$output['isSuccess']='0';
	$output['message']="COULDN'T SAVE TO LAST_GAMES";
}
else
	$output['isSuccess']='1';
echo json_encode($output);

?>
 