<?php

require_once 'bootstrap.php';
session_start();

// Si le formulaire est soumis pour connexion
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['connexion_submit']) && $_POST['connexion_submit'] == 1) {
    // Établir la connexion à la base de données
    $mysqli = connectDB();

    // Code de traitement du formulaire de connexion ici
    $mail_escaped = $mysqli->real_escape_string(trim($_POST['mail']));
    $password_plain = trim($_POST['password']);  // Mot de passe non échappé

    $sql = "SELECT id, password
            FROM user
            WHERE email = '" . $mail_escaped . "'";

    $result = $mysqli->query($sql);
    if (!$result) {
        $error = "Erreur lors de la vérification de l'adresse e-mail. Veuillez réessayer.";
        exit($mysqli->error);
    }

    $nb = $result->num_rows;
    if ($nb) {
        // Récupération de l'id de l'utilisateur et du mot de passe haché
        $row = $result->fetch_assoc();
        $hashed_password_stored = $row['password'];

        // Vérification du mot de passe avec crypt
        if (password_verify($password_plain, $hashed_password_stored)) {
            $_SESSION['compte'] = $row['id'];

            $sqlUser = "SELECT id, first_name, last_name, email, password, year FROM user WHERE id = {$row['id']}";
            $resultUser = $mysqli->query($sqlUser);
            $nbUser = $resultUser->num_rows;
            if ($nbUser) {
                $rowUser = $resultUser->fetch_assoc();
                header("Location: ./needLog/Calendrier.php");
            }
        } else {
            $error = "Vérifiez votre adresse e-mail et votre mot de passe, puis réessayez.";
        }
    } else {
        $error = "Vérifiez votre adresse e-mail et votre mot de passe, puis réessayez.";
    }

    // Fermer la connexion après avoir terminé le traitement
    $mysqli->close();
}

if (isset($_GET['logout']) && $_GET['logout'] == 1) {
    unset($_SESSION['compte']);
    header("Location: /index.html");
}

?>
