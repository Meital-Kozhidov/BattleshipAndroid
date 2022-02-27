<?php
 
/*Searches for opponent for "username": checking if active_players have rows (other than "username" itself). if not- add "username" to active_players. else- found an opponent. delete the opponent from active_players. create new active_games row for this new game.*/

//getting user values
$username=$_POST['username'];

//array of responses
$output=array();
 
//require database
require_once('db.php');

//checking if there's an active user
$conn=$dbh->prepare('SELECT username FROM active_players WHERE username!=? limit 1');
$conn->bindParam(1,$username);
$conn->execute();
 
//no active users in active_players
if($conn->rowCount() ==0){
	$output['isSuccess'] = 0;
	$output['message'] = "NO ACTIVE PLAYERS- WAITING...";

	$con=$dbh->prepare('SELECT username FROM active_players WHERE username= ?');
	$con->bindParam(1,$username);
	$con->execute();

	//either first time searching, or already found an opponent!
	if($con->rowCount()==0){
		$conn=$dbh->prepare('SELECT op2 FROM active_games WHERE op1= ?');
		$conn->bindParam(1,$username);
		$conn->execute();

		//first time searching
		if($conn->rowCount()==0){
			$conn=$dbh->prepare('INSERT INTO active_players(username) VALUES (?)');
			$conn->bindParam(1,$username);
			$conn->execute();}
		//found an opponent!
		else{
			$results=$conn->fetch(PDO::FETCH_OBJ);
			$op2=$results->op2;
			$output['isSuccess'] = 1;
			$output['message'] = "FOUND AN OPPONENT!";
			$output['opponent1']=$username;
			$output['opponent2']=$op2;	}
	}
}
 

//found activer user in active_players
else{
	$results=$conn->fetch(PDO::FETCH_OBJ);
	$first_player=$results->username;

	//deleting the user who waited for me from active_players
	$conn=$dbh->prepare('DELETE FROM active_players WHERE username=?');
	$conn->bindParam(1,$first_player);
	$conn->execute();
	$output['isSuccess'] = 1;
	$output['message'] = "FOUND AN OPPONENT!";
	$output['opponent1']=$first_player;
	$output['opponent2']=$username;

	//save this game in active_games
	$conn=$dbh->prepare('INSERT INTO active_games(op1,op2,last_played) VALUES (?,?,?)');
	$last_played="not yet";
	$conn->bindParam(1,$first_player);
	$conn->bindParam(2,$username);
	$conn->bindParam(3,$last_played);
	$conn->execute();
}

echo json_encode($output);
 
?>
 