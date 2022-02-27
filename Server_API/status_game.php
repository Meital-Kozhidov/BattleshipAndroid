<?php
 
/*Gets the row of this user's game from active_games. returns the info. if the game isn't found- other user exited and deleted it, so just return the status as 'exit'.*/

$username=$_GET['username'];
$whichOp=$_GET['whichOp'];

//array of responses
$output=array();
 
//require database
require_once('db.php');


if($whichOp==1)
	$con=$dbh->prepare('SELECT * FROM active_games WHERE op1= ?');
else
	$con=$dbh->prepare('SELECT * FROM active_games WHERE op2= ?');
$con->bindParam(1,$username);
$con->execute();

//the game isn't in active_games - other op exited- return to user the status 'exit'
if($con->rowCount() == 0)
	$output['last_status']='exit';

//the game exists in active_games
else{
	$results=$con->fetch(PDO::FETCH_OBJ);
	$last_played=$results->last_played;
	$hitOrMiss = $results->last_hitOrMiss;
	$hit_location = $results->hit_location;
	$status = $results->last_status;

	$output['hit_location']=$hit_location;
	$output['last_played']=$last_played;
	$output['last_hitOrMiss']=$hitOrMiss;
	$output['last_status']=$status;
}
echo json_encode($output);

 
?>
 