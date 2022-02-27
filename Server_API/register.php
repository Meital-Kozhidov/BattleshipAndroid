<?php
 
/*Registers new user - searches for username in users. if not found, creates new row in users with the user's params, plus creates new row for this user in last_games. else, returns the error message.*/

//getting user values
$username=$_POST['username'];
$password=$_POST['password'];
 
//array of responses
$output=array();
 
//require database
require_once('db.php');
 
//checking if username already exists
$conn=$dbh->prepare('SELECT username FROM users WHERE username= ?');
$conn->bindParam(1,$username);
$conn->execute();
 
//username already exists- return message for the user to login
if($conn->rowCount() !==0){
	$output['isSuccess'] = 0;
	$output['message'] = "Username Exists- Please Login";
}

//username doesn't exist so we insert it and the password to the users table
//also we add a row for this user in last_games table
else{ 
	$conn=$dbh->prepare('INSERT INTO users(username,password) VALUES (?,?)');

	//encrypting the password
	$pass=md5($password);

	$conn->bindParam(1,$username);
	$conn->bindParam(2,$pass); 
	$conn->execute();

	if($conn->rowCount() == 0){
		$output['isSuccess'] = 0;
		$output['message'] = "Registration failed, Please try again";
	}
	else{
		$con=$dbh->prepare('INSERT INTO last_games(username) VALUES (?)');
		$con->bindParam(1,$username);
		$con->execute();
		if($con->rowCount() == 0){
			$output['isSuccess'] = 0;
			$output['message'] = "CAN'T SAVE TO LAST_GAMES";}
		else{
			$output['isSuccess'] = 1;
			$output['message'] = "Succefully Registered";}
	}
}

echo json_encode($output);
 
?>
 