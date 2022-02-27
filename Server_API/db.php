<?php

/* Connect to db */ 

try{
	$host="mysql:host=localhost;dbname=battleship";
	$user_name="root";
	$user_password="";
	$dbh=new PDO($host,$user_name,$user_password);
}

/* Connection to db error */ 
catch(Exception $e){
	exit("Connection Error".$e->getMessage());
}
 
?>