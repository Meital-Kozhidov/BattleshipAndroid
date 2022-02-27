<?php
 
/*updating active_games table's row of current game: we find the current game by "last_played" column. User updates the table in his own turn- meaning, when "last_played" column is set to otherOp.*/

$username=$_POST['username'];
$hit_location=$_POST['hit_location'];
$hitOrMiss=$_POST['last_hitOrMiss'];
$whichOp=$_POST['whichOp'];
$otherOp=$_POST['otherOp'];

//array of responses
$output=array();
 
//require database
require_once('db.php');

//this is the first round for op1- last_played is set as "not yet".
if($hitOrMiss==""){
	$played="not yet";
	$con=$dbh->prepare('UPDATE active_games SET 	last_played=?,hit_location=?,last_hitOrMiss=? WHERE last_played= 	? and op1=?');
	$con->bindParam(1,$username);
	$con->bindParam(2,$hit_location);
	$con->bindParam(3,$hitOrMiss);
	$con->bindParam(4,$played);
	$con->bindParam(5,$username);
}

else{
$con=$dbh->prepare('UPDATE active_games SET last_played=?,hit_location=?,last_hitOrMiss=? WHERE last_played= ?');
$con->bindParam(1,$username);
$con->bindParam(2,$hit_location);
$con->bindParam(3,$hitOrMiss);
$con->bindParam(4,$otherOp);
}
$con->execute();
if($con->rowCount()==0){
	$output['isSuccess']='0';
	$output['message']='exit';
}
else{
	$output['isSuccess']='1';
	$output['message']="updated";
}
echo json_encode($output);


 
?>
 