<?php

include __DIR__ .'/../../scripts/bootstrap.php';
include __DIR__ .'/../../scripts/Calendar/Week.php';
include __DIR__ .'/../../scripts/Calendar/Events.php'; 
include __DIR__ .'/../../scripts/Team/Teams.php';    

sess_exists();

$mysqli = connectDB();
$events = new TimeFusion\Calendar\Events($mysqli);
$week = new TimeFusion\Calendar\Week($_GET['week'] ?? null, $_GET['year'] ?? null); // Utilisez la classe Week
$teams = new TimeFusion\Team\Teams($mysqli);
$start = $week->getFirstDay();
$end = $week->getLastDay();
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['users_id'])) {
    $usersId = explode(',', $_POST['users_id']);
} elseif ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['team_id'])) {
    $usersId = $teams->getMembersByTeamId($_POST['team_id']);
} else {
    $usersId = [$_SESSION['compte']];
}
$events = $events->getCoEventsBetweenByDay($start, $end, $usersId);
include __DIR__ .'/../../includes/header.php';
?>

<div class="calendar">
    <div class='d-flex flex-row align-items-center justify-content-between mx-sm-3'>
        <h1><?= $week->__toString(); ?></h1>
        <div class='d-flex flex-row'>
            <form action="Calendrier.php" method="post">
                <input type="hidden" name="users_id" value="<?= implode(',', $usersId); ?>">
                <button type="submit" class='btn btn-primary'>Mois</button>
            </form>
            <form action="CalendrierWeek.php" method="post">
                <input type="hidden" name="users_id" value="<?= implode(',', $usersId); ?>">
                <button type="submit" class='btn btn-primary'>Semaine</button>
            </form>
        </div>
        <div>
            <a href="CalendrierWeek.php?week=<?= $week->previousWeek()->week; ?>&year=<?= $week->previousWeek()->year; ?>" class="btn btn-primary">&lt;</a>
            <a href="CalendrierWeek.php?week=<?= $week->nextWeek()->week; ?>&year=<?= $week->nextWeek()->year; ?>" class="btn btn-primary">&gt;</a>
        </div>
    </div>

    <table class="calendar__table calendar__table--1week">
        <tr>
            <th></th> <!-- Ajoutez une cellule vide pour l'espace des heures -->
            <?php 
                foreach ($week->days as $k => $day): 
                $date = (clone $start)->modify("+".($k). "days");
                $eventsForDay = $events[$date->format('Y-m-d')] ?? [];    
            ?>
                <th>
                    <div class="calendar__day">
                        <span class="calendar__day-name"><?= $day; ?></span>
                        <span class="calendar__day-date"><?= $date->format('d'); ?></span>
                    </div>
                </th>
            <?php endforeach; ?>
        </tr>
        <?php
        $startOfWeek = clone $start;
        $hour = new DateTime('00:00');
        $endHour = new DateTime('23:59');
        $interval = new DateInterval('PT2H'); // Intervalle d'une heure
        ?>
        <?php while ($hour <= $endHour): ?>
            <tr>
                <td class="calendar__hour"><?= $hour->format('H:i'); ?></td>
                <?php for ($i = 0; $i < 7; $i++): ?>
                    <?php
                    $currentDate = clone $startOfWeek;
                    $currentDate->modify("+$i day");

                    // Extraire les événements pour la date actuelle
                    $eventsForDate = $events[$currentDate->format('Y-m-d')] ?? [];

                    // Vérifier si un événement se chevauche avec l'heure actuelle
                    $eventData = '';
                    foreach ($eventsForDate as $event) {
                        $eventStart = new DateTime($event['start_time']);
                        $eventEnd = new DateTime($event['end_time']);
                        if ($eventStart->format('H:i:s') <= $hour->format('H:i:s') && $eventEnd->format('H:i:s') > $hour->format('H:i:s')) {
                            $eventData .= "<div class='calendar__event'>{". h($event['title']). "}</div>";
                        }
                    }
                    ?>
                    <td class="<?= $week->withinWeek($startOfWeek) ? '' : 'calendar__otherweek'; ?>">
                        <?= $eventData; ?>
                    </td>
                <?php endfor; ?>
            </tr>
            <?php
            $hour->add($interval);
        endwhile;
        ?>


    </table>

    <a href="add.php" class="calendar__button">+</a>
</div>

<?php 
include __DIR__ .'/../../includes/footer.php';
?>
