<?php

/*Getting user "username" wins count from users table*/

//getting user values
$username=$_GET['username'];
 
//an array of response
$output = array();
 
//requires database connection
require_once('db.php');
 
//getting the number of user's wins and return them
$conn=$dbh->prepare("SELECT wins FROM users WHERE username= ?");
$conn->bindParam(1,$username);
$conn->execute();
$results=$conn->fetch(PDO::FETCH_COLUMN);
 
$output['wins'] = $results;

echo json_encode($output);
 
?>