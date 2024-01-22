<?php

include __DIR__ .'/../../scripts/bootstrap.php';
include __DIR__ .'/../../scripts/Calendar/Events.php';

sess_exists();

$mysqli = connectDB();
$events = new TimeFusion\Calendar\Events($mysqli);

if(!isset($_GET['id'])){
    e404();
}
try{
    $event = $events->find($_GET['id']);
} catch(\Exception $e){
    e404();
}

include __DIR__ .'/../../includes/header.php';
?>

<h1>Evènement: <?= h($event->getTitle()); ?></h1>

<ul>
    <li>Date: <?= $event->getStartTime()->format('d/m/Y'); ?></li>
    <li>Heure de début: <?= $event->getStartTime()->format('H:i'); ?></li>
    <li>Heure de fin: <?= $event->getEndTime()->format('H:i'); ?></li>
    <li>
        Description:<br>
        <?=h($event->getDescription()); ?>
    </li>
</ul>



<?php 
include __DIR__ .'/../../includes/footer.php';
?>