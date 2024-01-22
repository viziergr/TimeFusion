<?php 

include __DIR__ .'/../../scripts/bootstrap.php';
include __DIR__ .'/../../scripts/Team/Teams.php';  

sess_exists();

include __DIR__ .'/../../includes/header.php';

$data = [];
$errorMessage = '';

$mysqli = connectDB();
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = $_POST;
    $userId = $_SESSION['compte'];

    if (isset($_POST['name']) && isset($_POST['color']) && !empty($_POST['name']) && estCouleurValide($_POST['color'])) {
        $teams = new TimeFusion\Team\Teams($mysqli);
        $team = $teams->hydrate(new TimeFusion\Team\Team(), $data);
        if ($team == null) {
            $errorMessage = 'Une équipe avec ce nom existe déjà.';
        } else {
            $createResult = $teams->create($team, $userId);

            if ($createResult === true) {
                header('Location: Calendrier.php?success=3');
                exit();
            } else {
                $errorMessage = 'Une erreur est survenue lors de la création de l\'équipe.';
            }
        }
    } elseif (isset($_POST['name']) && isset($_POST['color']) && (empty($_POST['name']) || !estCouleurValide($_POST['color']))) {
        $errorMessage = 'Merci de corriger vos erreurs';
    }
}

?>

<?php if($errorMessage != ''): ?>
    <div class="alert alert-danger">
        <?= $errorMessage; ?>
    </div> 
<?php endif; ?>

<div class="container mt-5">
    <h2 class="mb-4">Créer une nouvelle équipe</h2>
    <form action="addTeam.php" method="POST">
        <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="name">Nom de l'équipe</label>
                    <input type="text" required name="name" id="name" class="form-control" value="<?= isset($data['name']) ? $data['name'] : '' ?>">
                    <?php if(isset($errors['name'])): ?>
                        <small class="text-danger"><?= $errors['name']; ?></small>
                    <?php endif; ?>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="color">Couleur</label>
                    <input type="color" required name="color" id="color" class="form-control" value="<?= isset($data['color']) ? $data['color'] : '' ?>">
                    <?php if(isset($errors['color'])): ?>
                        <small class="text-danger"><?= $errors['color']; ?></small>
                    <?php endif; ?>
                </div>
            </div>
        </div>
        <div class="form-group text-center">
            <button class="btn btn-primary btn-block mt-3">Créer l'équipe</button>
        </div>
    </form>
</div>

<?php include __DIR__ .'/../../includes/footer.php'; ?>
