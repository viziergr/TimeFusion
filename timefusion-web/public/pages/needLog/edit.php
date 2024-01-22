<?php
include __DIR__ .'/../../scripts/bootstrap.php';
include __DIR__ .'/../../scripts/Calendar/EventValidator.php';
include __DIR__ .'/../../scripts/Calendar/Events.php'; 

sess_exists();

$mysqli = connectDB();
$events = new TimeFusion\Calendar\Events($mysqli);
$errors = [];

if(!isset($_GET['id'])){
    e404();
}
try{
    $event = $events->find($_GET['id']);
    $participants = $events->getParticipants($event->getId());
} catch(\Exception $e){
    e404();
}

$data = [
    'name' => $event->getTitle(),
    'date' => $event->getStartTime()->format('Y-m-d'),
    'start' => $event->getStartTime()->format('H:i'),
    'end' => $event->getEndTime()->format('H:i'),
    'description' => $event->getDescription()
];

if($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = $_POST;
    $validator = new TimeFusion\Calendar\EventValidator();
    $errors = $validator->validates($data);
    if(empty($errors)) {
        $events->hydrate($event,$data,$_SESSION['compte']);
        $events->update($event);
        header('Location: /PHP/public/Calendrier.php?success=2');
        exit();
    }
}

include __DIR__ .'/../../includes/header.php';
?>

<div class="container">
    <h1>Editer l'évènement: <small><?= h($event->getTitle()); ?></small></h1>
    <form action="" method="POST">
        <?php render('calendar/form', ['data' => $data, 'errors' => $errors]); ?>
    </form>
</div>

<div class="container">
    <small>Ajouter des participants:</small>
    <form action="networkpanel.php" method="GET">
        <input type="hidden" name="event_panel" value="1">
        <input type="hidden" name="event_id" value="<?= $event->getId(); ?>">
        <button type="submit">Ajouter des participants</button>
    </form>
    <?php foreach($participants as $participant): ?>
        <div class="user-card">
            <h3><?= h($participant['first_name']) . ' ' . h($participant['last_name']); ?></h3>
            <p>Email: <?= h($participant['email']); ?></p>
            <p>Year: <?= h($participant['year']); ?></p>
        </div>
    <?php endforeach; ?>
</div>




<?php 
include __DIR__ .'/../../includes/footer.php';
?>