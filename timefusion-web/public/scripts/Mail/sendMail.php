<?php

function reinitPassword($email) {

    $sujet = "Réinitialisation du mot de passe";
    $message = "Bonjour,\n\n";
    $message .= "Vous avez demandé la réinitialisation de votre mot de passe.\n";
    $message .= "Cliquez sur le lien suivant pour réinitialiser votre mot de passe :";
    $message .= "http://localhost/PHP/public/reinitPassword.php\n";
    $message .= "Si vous n'avez pas demandé cette réinitialisation, ignorez cet email.\n\n";
    $message .= "L'équipe TimeFusion";
        
    $headers = "Content-Type: text/plain; charset=utf-8\r\n";
    $headers .= "From: timefusion.calendar@gmail.com\r\n";       
        
    if (mail($email, $sujet, $message, $headers)) {
        echo "Un email a été envoyé à $email avec les instructions pour réinitialiser votre mot de passe.";
    } else {
        echo "Une erreur s'est produite lors de l'envoi de l'email.";
    }
}

function acceptEvent($prenom, $nom, $user) {

    $sujet = "On souhaite vous inviter à un événement";
    $message = "Bonjour,\n\n";
    $message .= "Vous avez été invité à l'événement suivant par " . $prenom . " " . $nom . ":\n";
    $message .= "Nom : " . $event['name'] . "\n";
    $message .= "Date : " . $event['date'] . "\n";
    $message .= "Heure : " . $event['time'] . "\n";
    $message .= "Description : " . $event['description'] . "\n\n";
    $message .= "L'équipe TimeFusion";
        
    $headers = "Content-Type: text/plain; charset=utf-8\r\n";
    $headers .= "From : timefusion.calendar@gmail.com\r\n";

    if (mail($email, $sujet, $message, $headers)) {
        echo "Vous avez invité $email à votre évenement";
    } else {
        echo "Une erreur s'est produite lors de l'envoi de l'email.";
    }
}
?>

