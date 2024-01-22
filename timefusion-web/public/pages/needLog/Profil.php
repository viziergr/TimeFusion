<?php 

include __DIR__ . '/../../scripts/gestion_inscription.php'; 
include __DIR__ .'/../../scripts/Team/Users.php';

sess_exists();

$users = new TimeFusion\Team\Users(connectDB());
$user = $users->getUserById($_SESSION['compte']);
?>

<!DOCTYPE html>
<html>
<head>
    <title>Profil</title>
    <link rel="stylesheet" href="../../assets/CSS/code.css">
</head>
<body>

    <div class="bandeV">
        <div class="dropdown">
            <img id="trbar" src="/assets/pictures/trBarre.png" onclick="toggleDropdown()"/>
            <div class="dropdown-content">
                <a href="/pages/needLog/Calendrier.php">Mon calendrier</a><br>
                <a href="/pages/needLog/teampanel.php">Mes équipes</a><br>
                <a href="/pages/needLog/requestpanel.php">Mes notifications</a><br>
            </div>
        </div>
        <h1>TimeFusion</h1>
        <a href="/index.html">
            <img id ="logo" src="/assets/pictures/Logo.png">
        </a>
    </div>

    <div class="profil">
        <h1>Profil</h1>
    </div>
    
    <div class="information">
        <div class="traitV"></div>

            <?php        

            echo '<p id="fname"> Prénom : ' . h($user->getFirstName()) . '</p>';

            echo '<p id="lname"> Nom : ' . h($user->getLastName()) . '</p>';

            echo '<p id="email"> Email : ' . h($user->getEmail()) . '</p>';

            echo '<p id="year"> Date d\'anniversaire : ' . h((new \DateTime($user->getYear()))->format('d/m/Y')) . '</p>';

            ?>

        </div>
        <div class="modif">
            <a href="newPassword.php">
                <p> Modifier votre mot de passe </p>
            </a>
        </div>
    </div>

</body>
</html>
