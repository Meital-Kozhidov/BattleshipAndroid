<?php

/*Returns result of 10 last games of user "username" from last_games table*/
 
//getting user values
$username=$_GET['username'];

//an array of response
$output = array();
 
//requires database connection
require_once('db.php');
 
//getting last_games of user
$conn=$dbh->prepare("SELECT * FROM last_games WHERE username= ?");
$conn->bindParam(1,$username);
$conn->execute();

if($conn->rowCount() == 0){
	$output['isSuccess'] = 0;
	$output['message'] = "Error: username doesn't exist";
}
 
else{
	$results=$conn->fetch(PDO::FETCH_NUM);	 
	$output['isSuccess'] = 1;
	$output['games']=$results;
}

echo json_encode($output);
 
?>