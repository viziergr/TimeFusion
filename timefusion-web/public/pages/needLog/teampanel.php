<?php

include __DIR__ .'/../../scripts/bootstrap.php';
include __DIR__ .'/../../scripts/Team/Teams.php';  

sess_exists();

$mysqli = connectDB();
$teams = new TimeFusion\Team\Teams($mysqli);

// Supposons que vous ayez un mécanisme d'authentification qui donne l'ID de l'utilisateur connecté
$userId = $_SESSION['compte']; // Assurez-vous de récupérer l'ID de l'utilisateur correctement

$userTeams = $teams->getUserTeams($userId);

include __DIR__ .'/../../includes/header.php';
?>

<div class="grid-container">
    <?php 
    // Utilisez $userTeams pour afficher les équipes
    foreach ($userTeams as $team) {
    ?>
        <div class="team-container" style="background-color: <?php echo $team->getColor(); ?>;">
            <div class="team-header">
                <h1>Equipe: <small><?php echo h($team->getName()); ?></small></h1>
            </div>
            <h3>Membres:</h3>
            <?php
            $members = $team->getMembers();
            foreach ($members as $member) {
                echo h($member['role']) . ': ' . h($member['user']->getFullName()) . '<br>';
            }
            ?>
            
            <!-- Bouton "Ajouter un membre" qui redirige vers networkpanel.php avec l'ID de la team -->
            <div class="team-actions">
                <form action="networkpanel.php" method="get">
                    <input type="hidden" name="team_id" value="<?php echo $team->getId(); ?>">
                    <button type="submit">Ajouter un membre</button>
                </form>

                <!-- Bouton "Afficher l'agenda commun" (ajoutez ici le lien vers la page d'agenda commun) -->
                <form action="Calendrier.php" method="post">
                    <input type="hidden" name="team_id" value="<?php echo $team->getId(); ?>">
                    <button type="submit">Afficher l'agenda commun</button>
                </form>

                <form action="insideteampanel.php" method="get">
                    <input type="hidden" name="team_id" value="<?php echo $team->getId(); ?>">
                    <button type="submit">Gérer l'équipe</button>
                </form>
            </div>
        </div>
    <?php
    }
    ?>
</div>
<a href="addTeam.php" class="calendar__button">+</a>

<?php
include __DIR__ .'/../../includes/footer.php';
?>