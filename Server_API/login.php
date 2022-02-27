<?php

/*Checks username and password in users table and returns if the details are correct*/
 
//getting user values
$username=$_POST['username'];
$password=$_POST['password'];
 
//an array of response
$output = array();
 
//requires database connection
require_once('db.php');
 
//searching in users table this username and password
$conn=$dbh->prepare("SELECT * FROM users WHERE username= ? and password= ?");
$pass=md5($password);
$conn->bindParam(1,$username);
$conn->bindParam(2,$pass);
$conn->execute();
if($conn->rowCount() == 0){
	$output['isSuccess'] = 0;
	$output['message'] = "Wrong username or password";
}

else{ 
	$output['isSuccess'] = 1;
	$output['message'] = "Login successful"; 
}
echo json_encode($output);
 
?>