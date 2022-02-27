<?php

/* Deleting row from active_players: user "username" found opponent, so
no longer needs to be in active_players*/
 
//getting user values
$username=$_POST['username'];

//array of responses
$output=array();
 
//require database
require_once('db.php');

$conn=$dbh->prepare('DELETE FROM active_players WHERE username=?');
$conn->bindParam(1,$username);
$conn->execute();

//can't find row
if($conn->rowCount() == 0){
	$output['isSuccess'] = 0;
	$output['message'] = "Error: username doesn't exist in active_players";
}
 
else{	 
	$output['isSuccess'] = 1;
}
echo json_encode($output);

 
?>
 