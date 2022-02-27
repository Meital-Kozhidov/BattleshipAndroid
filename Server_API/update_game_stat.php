<?php

/*Updating username's row in active_games table column last_status to "win" - to let the other op know he won (=other op)*/

$username=$_POST['username'];
$whichOp=$_POST['whichOp'];

//array of responses
$output=array();
 
//require database
require_once('db.php');

$status="win";

if($whichOp==1)
	$con=$dbh->prepare('UPDATE active_games SET last_status=? WHERE op1= ?');
	
else
	$con=$dbh->prepare('UPDATE active_games SET last_status=? WHERE op2= ?');
	
$con->bindParam(2,$username);
$con->bindParam(1,$status);
$con->execute();
$output['message']='exited';
echo json_encode($output);
 
?>
 