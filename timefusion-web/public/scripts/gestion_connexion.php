<?php

require_once 'bootstrap.php';
session_start();

// Si le formulaire est soumis pour connexion
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['connexion_submit']) && $_POST['connexion_submit'] == 1) {
    // Établir la connexion à la base de données
    $mysqli = connectDB();

    // Code de traitement du formulaire de connexion ici
    $mail_escaped = $mysqli->real_escape_string(trim($_POST['mail']));
    $password_escaped = $mysqli->real_escape_string(trim($_POST['password']));

    $sql = "SELECT id
                FROM user
                WHERE email = '" . $mail_escaped . "'
                AND password = '" . $password_escaped . "'";

    $result = $mysqli->query($sql);
    if (!$result) {
        $error = "Vérifiez votre adresse e-mail et votre mot de passe, puis réessayez.\nIl se peut que vous n'ayez pas créé de compte.";
        exit($mysqli->error);
    }

    $nb = $result->num_rows;
    if ($nb) {
        //récupération de l’id de l’étudiant
        $row = $result->fetch_assoc();
        $_SESSION['compte'] = $row['id'];

        $sqlEtudiant = "SELECT id, first_name, last_name, email, password, year FROM user WHERE id = {$row['id']}";
        $resultEtudiant = $mysqli->query($sqlEtudiant);
        $nbEtu = $resultEtudiant->num_rows;
        if ($nbEtu) {
            $rowEtu = $resultEtudiant->fetch_assoc();
            header("Location: ./needLog/Calendrier.php");
        }
    }
    else{
        $error = "Vérifiez votre adresse e-mail et votre mot de passe, puis réessayez.\nIl se peut que vous n'ayez pas créé de compte.";
    }

    // Fermer la connexion après avoir terminé le traitement
    $mysqli->close();
}

if (isset($_GET['logout']) && $_GET['logout'] == 1) {
    unset($_SESSION['compte']);
    header("Location: /index.html");
}

?>