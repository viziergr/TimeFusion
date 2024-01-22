<?php

require_once("bootstrap.php");

if (isset($_POST['inscription_submit']) && $_POST['inscription_submit'] == 2) {
    // Gestion de l'inscription
    $mysqli = connectDB();

    if (
        isset($_POST['fname']) && isset($_POST['lname']) && isset($_POST['year']) &&
        isset($_POST['mail']) && isset($_POST['pwd']) && isset($_POST['cpwd'])
    ) {
        $nom = $_POST['fname'];
        $prenom = $_POST['lname'];
        $annee = $_POST['year'];
        $email = $_POST['mail'];
        $pwd = $_POST['pwd'];
        $cpwd = $_POST['cpwd'];

        // Validation de l'e-mail
        $email = filter_var($email, FILTER_VALIDATE_EMAIL);
        if ($email === false) {
            $error = "L'adresse e-mail n'est pas valide.";
        }

        // Validation du mot de passe (au moins 8 caractères, une lettre, un chiffre et un caractère spécial)
        if (strlen($pwd) < 8 || !preg_match("/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/", $pwd)) {
            $error = "Le mot de passe doit contenir au moins 8 caractères, une lettre, un chiffre et un caractère spécial.";
        }

        // Vérification de l'existence de l'email dans la base de données
        $mail_escaped = $mysqli->real_escape_string(trim($email));
        $check_email_query = "SELECT * FROM user WHERE email = '$mail_escaped'";
        $email_result = $mysqli->query($check_email_query);

        $hashedPassword = crypt($pwd, '$2y$12$' . bin2hex(random_bytes(22)));
        $password_escaped = $mysqli->real_escape_string(trim($hashedPassword));
        
        if ($email_result->num_rows > 0) {
            // L'email existe déjà dans la base de données
            $error = "Cet e-mail est déjà associé à un compte existant.";
        } else {
            if ($pwd == $cpwd) {
                // Requête SQL pour insérer l'étudiant dans la table
                $id_defined = false;
                while (!$id_defined) {
                    try {
                        $id = rand();
                        $hashedPassword = password_hash($pwd, PASSWORD_DEFAULT);
                        $sql = "INSERT INTO user (id, first_name, last_name, email, password, year) VALUES ('$id','$nom', '$prenom', '$email', '$hashedPassword', '$annee')";
                        $mysqli->query($sql);
                        $id_defined = true;
                        header("Location: ./needLog/Calendrier.php");
                    } catch (mysqli_sql_exception $e) {
                        echo "" . $e->getMessage() . "";
                    }
                }
            } else {
                $error = "Le mot de passe n'est pas le même.";
            }
        }
    } else {
        $error = "Tous les champs du formulaire doivent être remplis.";
    }

    $mysqli->close();
}

?>
