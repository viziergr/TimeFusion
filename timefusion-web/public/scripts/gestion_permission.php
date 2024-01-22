<?php

include 'bootstrap.php';
include 'Team/Teams.php';

$teams = new TimeFusion\Team\Teams(connectDB());

if($_SERVER['REQUEST_METHOD'] === 'POST'&& isset($_POST['quit']) && isset($_POST['member_id']) && isset($_POST['team_id'])) {
    $member_id = $_POST['member_id'];
    $team_id = $_POST['team_id'];
    try{
        $teams->removeMember($team_id,$member_id);
        header('Location: ../pages/needLog/insideteampanel.php?team_id='.$team_id.'');
        exit();
    }catch(\Exception $e){
        header('Location: ../pages/needLog/insideteampanel.php?team_id='.$team_id.'&error=nq');
        exit();
    }
}
if($_SERVER['REQUEST_METHOD'] === 'POST'&& isset($_POST['promote']) && isset($_POST['member_id']) && isset($_POST['team_id'])) {
    $member_id = $_POST['member_id'];
    $team_id = $_POST['team_id'];
    $teams->promote($team_id,$member_id);
    header('Location: ../pages/needLog/insideteampanel.php?team_id='.$team_id.'');
    exit();
}
if($_SERVER['REQUEST_METHOD'] === 'POST'&& isset($_POST['relegate']) && isset($_POST['member_id']) && isset($_POST['team_id'])) {
    $member_id = $_POST['member_id'];
    $team_id = $_POST['team_id'];
    $teams->relegate($team_id,$member_id);
    header('Location: ../pages/needLog/insideteampanel.php?team_id='.$team_id);
    exit();
}

?>