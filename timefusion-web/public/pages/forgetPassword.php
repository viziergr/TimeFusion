<?php include __DIR__ . '/../scripts/bootstrap.php'; ?>
<?php include __DIR__ . '/../scripts/Mail/sendMail.php'; ?>

<!DOCTYPE html>
<html lang=en>
<head>
    <title>Réinitialisation du mot de passe</title>
    <link rel="stylesheet" href="..\..\CSS\code.css">
    <style>
        input[type="email"] {
            background-color: transparent;
            border: none;
            outline: none;
        }
    </style>
</head>
<body>
    <div class="bande">
        <a href="index.html">
            <img id="home" src="..\..\pictures\home.png" alt="Accueil">
        </a>
    </div>
    
    <div class="envoiMail">
        <h1>Problèmes de connexion ?</h1>
        <p>Entrez votre adresse e-mail et nous vous enverrons un lien pour réinitialiser votre mot de passe.</p>

        <form method="post">
            <input type="email" id="email" name="email" placeholder="E-mail" required><br><br>
            <button id="boutonEnvoi" type="submit">Envoyer</button>
        </form>
    </div>
</body>
</html>

<?php
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['email'])) {
        $email = $_POST['email'];

        $mysqli = connectDB();

        $mail_escaped = $mysqli->real_escape_string(trim($email));

        $check_email_query = "SELECT * FROM User WHERE email = '$mail_escaped'";
        $email_result = $mysqli->query($check_email_query);
        if ($email_result->num_rows > 0) {    
        
        reinitPassword($email);

        } else {
            echo "Aucun compte n'est associé à cette adresse email.";
        }

        $mysqli->close();
    } else {
        echo "Veuillez fournir une adresse email.";
    }
}
?>
