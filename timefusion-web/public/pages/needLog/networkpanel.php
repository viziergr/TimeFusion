<?php

include __DIR__ .'/../../scripts/bootstrap.php';
include __DIR__ .'/../../scripts/Team/Users.php';  

sess_exists();

include __DIR__ .'/../../includes/header.php';

$mysqli = connectDB();
$users = new TimeFusion\Team\Users($mysqli);

$userId = $_SESSION['compte']; // Assurez-vous de récupérer l'ID de l'utilisateur correctement

$userList = $users->getEveryOtherUsers($userId);

?>

<?php if (isset($_GET['demande_envoyee']) && $_GET['demande_envoyee']==='1'): ?>
    <div class="container">
        <div class="alert alert-success">
            La demande d'ajout a bien été envoyée.
        </div>
    </div>
<?php elseif (isset($_GET['demande_envoyee']) && $_GET['demande_envoyee']==='2'): ?>
    <div class="container">
        <div class="alert alert-danger">
            La demande d'ajout n'a pas été envoyée. Attendez que l'utilisateur réponde à votre invitation ou rééssayez plus tard. 
        </div>
    </div>
<?php endif; ?>

<div class="grid-container">
    <?php foreach ($userList as $user): ?>
        <div class="user-card">
            <h3><?= h($user->getFirstName()) . ' ' . h($user->getLastName()); ?></h3>
            <p>Email: <?= h($user->getEmail()); ?></p>
            <p>Year: <?= h($user->getYear()); ?></p>
            
            <!-- Formulaire avec le bouton "Ajouter à une équipe" -->
            <?php if(isset($_GET['event_panel']) && $_GET['event_panel']==='1' && isset($_GET['event_id'])):?>

                <form action='../../scripts/gestion_notifications.php' method="post">
                    <input type="hidden" name="user_id" value="<?= $user->getId(); ?>">
                    <input type="hidden" name="event_id" value="<?= $_GET['event_id']; ?>">
                    <button type="submit" name="add_event">Inviter à rejoindre l'évènement</button>
                </form>
            <?php else: ?>
            <form action='../../scripts/gestion_notifications.php' method="post">
                <input type="hidden" name="user_id" value="<?= $user->getId(); ?>">
                <?php 
                    if (isset($_GET['team_id'])){
                        $teamId = $_GET['team_id'];
                    }
                ?>
                <input type="hidden" name="team_id" value="<?= $teamId; ?>">
                <button type="submit" name="add_team">Ajouter à une équipe</button>
            </form>
            <?php endif; ?>
        </div>
    <?php endforeach; ?>
</div>

</body>
</html>

<?php
include __DIR__ .'/../../includes/footer.php';
?>