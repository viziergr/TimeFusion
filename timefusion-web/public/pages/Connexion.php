<?php include '../scripts/gestion_connexion.php'; ?>

<!DOCTYPE html>
<html lang="en">
<html>
<head>
    <title>Formulaire de connexion</title>
    <link rel="stylesheet" href="../assets/CSS/code.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <meta charset="utf-8">
</head>
<body>

    <div class="bande">
        <a href="../index.html">
            <img id="home" src="../assets/pictures/home.png" alt="Accueil">
        </a>
    </div>

    <?php if (empty($_SESSION['compte'])): ?>
    <div class="cnx">
        <img src="../assets/pictures/Logo.png" alt="Logo Time Fusion">    
        <h1>Bienvenue</h1>
        <?php if (isset($error)): ?>
            <div class="container">
                <div class="alert alert-danger">
                    <?= $error ?>
                </div>
            </div>
        <?php endif; ?>
        <form method="post">
            <div>          
                <input type="email" id="email" name="mail" placeholder="E-mail" required>
            </div>
            <div>
                <input type="password" id="password" name="password" placeholder="Mot de passe" required>
            </div>
            <div>
                <button type="submit" id="boutonConnexion" value="1" name="connexion_submit">Connexion</button>
            </div>
        </form>
    </div>    

    <div class = "redirectionInscription">  
        <p>Pas de compte ?</p>
        <a href="Inscription.php">
            <button type="button">Inscrivez vous</button>
        </a>
    </div>
    <div class="oubliMDP">
        <a href="forgetPassword.php">
            <button id="newMDP" type="button">Mot de passe oublié ?</button>
        </a>
    </div>
    <?php else:
        header('Location: ../pages/needLog/Calendrier.php');
    endif; ?>
</body>

</html>
