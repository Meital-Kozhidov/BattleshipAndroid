<?php

/*Gets the ten users from users table with the highest wins count*/

//an array of response
$output = array();
 
//requires database connection
require_once('db.php');
 
//Getting 10 top players from users
$conn=$dbh->prepare("SELECT * FROM users order by wins DESC limit 10");
$conn->execute();

if($conn->rowCount() == 0){
	$output['isSuccess'] = 0;
	$output['message'] = "no users in 'users' table";
}
 
else{
	$results=$conn->fetchAll(PDO::FETCH_OBJ);
	$output['isSuccess'] = 1;
	$output['results'] = $results;
}

echo json_encode($output);
 
?>