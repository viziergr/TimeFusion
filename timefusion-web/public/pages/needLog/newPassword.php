<?php require_once __DIR__ . '/../../src/bootstrap.php'; ?>
<?php require __DIR__ . '/../../views/header.php'; ?>

<DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Modifier le mot de passe</title>
    <link rel="stylesheet" href="/../../CSS\code.css">
</head>
<body>
    <div class="modifPassword">
        <h1>Modifier le mot de passe</h1>
        <form method="POST" action="">
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
            $newPassword = $_POST['new_password'];
            $confirmPassword = $_POST['confirm_password'];
            
            if ($newPassword === $confirmPassword) {
                // Hasher le nouveau mot de passe
                //$hashedPassword = password_hash($newPassword, PASSWORD_DEFAULT);

                $mysqli = connectDB();
                sess_exists();

                $id = $_SESSION["compte"]; // Remplacez par l'identifiant de l'utilisateur connecté
                $sql = "UPDATE user SET password = '$newPassword' WHERE id = $id";

                if ($mysqli->query($sql) === TRUE) {
                    echo "Mot de passe mis à jour avec succès.";
                } else {
                    echo "Erreur lors de la mise à jour du mot de passe : " . $mysqli->error;
                }

                $mysqli->close();
            } else {
                echo "Les mots de passe ne correspondent pas.";
            }
        }
        ?>