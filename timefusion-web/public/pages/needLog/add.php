<?php

include __DIR__ .'/../../scripts/bootstrap.php';
include __DIR__ .'/../../scripts/Calendar/EventValidator.php';
include __DIR__ .'/../../scripts/Calendar/Events.php';
include __DIR__ .'/../../scripts/Calendar/Event.php';  

sess_exists();

include __DIR__ .'/../../includes/header.php';

$data = [];
$errors = [];

$mysqli = connectDB();
if($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = $_POST;
    $userId = $_SESSION['compte'];
    $validator = new TimeFusion\Calendar\EventValidator();
    $errors = $validator->validates($_POST);
    if(empty($errors)) {
        $events = new TimeFusion\Calendar\Events($mysqli);
        $event = $events->hydrate(new TimeFusion\Calendar\Event(),$data, $userId);
        $events->create($event);
        header('Location: Calendrier.php?success=1');
        exit();
    }
}
?>

<?php if(!empty($errors)): ?>
    <div class="alert alert-danger">
        Merci de corriger vos erreurs
    </div> 
<?php endif; ?>


<div class="container">
    <h1>Ajouter un évènement</h1>
    <form action="" method="POST">
        <?php render('calendar/form', ['data' => $data, 'errors' => $errors]); ?>
    </form>
</div>


<?php
include __DIR__ .'/../../includes/footer.php';
?>