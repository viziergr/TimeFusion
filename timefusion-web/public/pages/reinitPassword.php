<?php include __DIR__ . '/../scripts/bootstrap.php'; ?>
<?php include __DIR__ . '/../scripts/gestion_reinit_mdp.php'; ?>

<DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Réinitialiser le mot de passe</title>
    <link rel="stylesheet" href="/assets/CSS/code.css">
</head>
<body>
    <div class="bande">
        <a href="index.html">
            <img id="home" src="/assets/pictures/home.png" alt="Accueil">
        </a>
    </div>

    <div class="modifPassword">
        <h1>Réinitialiser le mot de passe</h1>
        <p>Entrez votre adresse e-mail d'inscription et votre nouveau mot de passe</p>
        <form method="POST">
            <div>
                <input type="email" id="email" name="email" placeholder="E-mail d'inscription" required><br><br>
            </div>
            <div>
                <input type="password" id="pwd" name="new_password" placeholder="Nouveau mot de passe" required><br><br>
            </div>
            <div>    
                <input type="password" id="confPwd" name="confirm_password" placeholder="Confirmer mot de passe" required>
            </div>
            <div>
                <button type="submit">Modifier le mot de passe</button>
            </div>
        </form>
        
    </div>
</body>
</html>

<?php
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $email = $_POST['email'];
        $newPassword = $_POST['new_password'];
        $confirmPassword = $_POST['confirm_password'];
         
        if ($newPassword === $confirmPassword) {

            $mysqli = connectDB();

            $id = null;
            $mail_escaped = $mysqli->real_escape_string(trim($email));

            $check_email_query = "SELECT id FROM User WHERE email = '$mail_escaped'";
            $email_result = $mysqli->query($check_email_query);

            if ($email_result->num_rows > 0) {
                $row = $email_result->fetch_assoc();
                $id = $row['id'];

                $sql = "UPDATE user SET password = '$newPassword' WHERE id = $id";

                if ($mysqli->query($sql) === TRUE) {
                    echo "Mot de passe mis à jour avec succès.";
                } else {
                    echo "Erreur lors de la mise à jour du mot de passe : " . $mysqli->error;
                }

                $mysqli->close();
            } else {
                echo "Cet e-mail n'est associé à aucun compte existant.";
            }
        } else {
            echo "Les mots de passe ne correspondent pas.";

        }
    }
?>