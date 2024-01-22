<?php 

include __DIR__ .'/../../scripts/bootstrap.php';
include __DIR__ .'/../../scripts/Calendar/Month.php';
include __DIR__ .'/../../scripts/Calendar/Events.php'; 
include __DIR__ .'/../../scripts/Team/Teams.php';   

sess_exists();

$mysqli = connectDB();
$events = new TimeFusion\Calendar\Events($mysqli);
$month = new TimeFusion\Calendar\Month($_GET['month'] ?? null, $_GET['year'] ?? null);
$teams = new TimeFusion\Team\Teams($mysqli);
$start = $month->getFirstDay();
if ($start->format('N') !== '1') {
    $start->modify('last monday');
}
$end = (clone $start)->modify('+'. (4 * 7 -1).' days');
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['users_id'])) {
    $usersId = $_POST['users_id'];
}
elseif($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['team_id'])) {
    $usersId = $teams->getMembersByTeamId($_POST['team_id']);
}else{
    $usersId = [$_SESSION['compte']];
}
dd($_POST);
include __DIR__ .'/../../includes/header.php';

$events = $events->getCoEventsBetweenByDay($start,$end,$usersId);
?>

<div class="calendar">

    <?php if (isset($_GET['success'])): ?>
        <div class="container">
            <div class="alert alert-success"> 
                <?php if($_GET['success'] === '1'): ?>
                    L'évènement a bien été ajouté
                <?php elseif($_GET['success'] === '2'): ?>
                    L'évènement a bien été modifié
                <?php elseif($_GET['success'] === '3'): ?>
                    L'équipe a bien été créée
                <?php endif; ?>
            </div>
        </div>
    <?php endif; ?>

    <div class='d-flex flex-row align-items-center justify-content-between mx-sm-3'>
        <h1><?= $month->__toString(); ?></h1>
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
            <a href="Calendrier.php?month=<?= $month->previousMonth()->month; ?>&year=<?= $month->previousMonth()->year; ?>" class="btn btn-primary">&lt;</a>
            <a href="Calendrier.php?month=<?= $month->nextMonth()->month; ?>&year=<?= $month->nextMonth()->year; ?>" class="btn btn-primary">&gt;</a>
        </div>
    </div>
    <table class="calendar__table calendar__table--<?= $month->getWeeks(); ?>weeks">
        <?php for ($i = 0; $i < $month->getWeeks(); $i++): ?>
        <tr>
            <?php 
                foreach ($month->days as $k => $day): 
                $date = (clone $start)->modify("+".($k + $i*7). "days");
                $eventsForDay = $events[$date->format('Y-m-d')] ?? [];    
            ?>
            <td class="<?= $month->withinMonth($date) ? '' : 'calendar__othermonth'; ?>" >
                <?php if ($i === 0): ?>
                    <div class='calendar__weekday'><?= $day; ?></div>
                <?php endif; ?>
                <div class='calendar__day'><?= $date->format('d'); ?></div>
                <?php foreach ($eventsForDay as $event): ?>
                    <?php if ($event['is_private']==1 && $_SESSION['compte']!=$event['creator_id']) : ?>
                        <div class="calendar__event__private">
                            <?= (new \DateTime($event['start_time']))->format('H:i') ?> -
                            <span class="private-event">Événement Privé</span>
                        </div>
                    <?php else : ?>
                        <div class="calendar__event">
                            <?= (new \DateTime($event['start_time']))->format('H:i') ?> -
                            <a href="edit.php?id=<?= $event['id']; ?>"> <?= h($event['title']); ?></a>
                        </div>
                    <?php endif; ?>
                <?php endforeach; ?>
            </td>
            <?php endforeach; ?>
        </tr>
        <?php endfor; ?>
    </table>
    <a href="add.php" class="calendar__button">+</a>
</div>


<?php 
include __DIR__ .'/../../includes/footer.php';
?>