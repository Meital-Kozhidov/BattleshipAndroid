<?php
 
/* Deleting row from active_games: game is over or user "username" exited.
status is 'win' or 'exit'. if 'win' : user "username" won the game. save him a win.
if 'exit': user "username" exited.
both cases delete this game's row from active_games*/

$username=$_POST['username'];
$status=$_POST['last_status'];
$whichOp=$_POST['whichOp'];


//array of responses
$output=array();
 
//require database
require_once('db.php');

if($status=='win'){

$conn=$dbh->prepare("SELECT * FROM users WHERE username= ?");
$conn->bindParam(1,$username);
$conn->execute();
$results=$conn->fetch(PDO::FETCH_OBJ);
$wins=$results->wins;
$wins=$wins+1;

$con=$dbh->prepare('UPDATE users SET wins=? WHERE username= ?');
	$con->bindParam(2,$username);
	$con->bindParam(1,$wins);
	$con->execute();
}


if($whichOp==1){
	$con=$dbh->prepare('DELETE FROM active_games WHERE op1= ?');
}
else{
	$con=$dbh->prepare('DELETE FROM active_games WHERE op2= ?');
}

	$con->bindParam(1,$username);
	$con->execute();

if($con->rowCount() == 0)
	$output['message']='game already deleted';
else
	$output['message']='deleted';
echo json_encode($output);
 
?>
 
