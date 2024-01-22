<div class="row">
    <div class="col-sm-6">
        <div class="form-group">
            <label for="name">Nom de l'évènement</label>
            <input type="text" required name="name" id="name" class="form-control" value="<?= isset($data['name']) ? $data['name'] : '' ?>">
            <?php if(isset($errors['name'])): ?>
                <small class="form-text text-muted"><?= $errors['name']; ?></small>
            <?php endif; ?>
        </div>
    </div>
    <div class="col-sm-6">
        <div class="form-group">
            <label for="date">Date</label>
            <input type="date" required name="date" id="date" class="form-control" value="<?= isset($data['date']) ? $data['date'] : '' ?>">
            <?php if(isset($errors['date'])): ?>
                <small class="form-text text-muted"><?= $errors['date']; ?></small>
            <?php endif; ?>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-sm-6">
        <div class="form-group">
            <label for="start">Début</label>
            <input type="time" required name="start" id="start" class="form-control" placeholder="HH:MM" value="<?= isset($data['start']) ? $data['start'] : '' ?>">
            <?php if(isset($errors['start'])): ?>
                <small class="form-text text-muted"><?= $errors['start']; ?></small>
            <?php endif; ?>
        </div>
    </div>
    <div class="col-sm-6">
        <div class="form-group">
            <label for="end">Fin</label>
            <input type="time" required name="end" id="end" class="form-control" placeholder="HH:MM" value="<?= isset($data['end']) ? $data['end'] : '' ?>">
            <?php if(isset($errors['end'])): ?>
                <small class="form-text text-muted"><?= $errors['end']; ?></small>
            <?php endif; ?>
        </div>
    </div>
</div>
<div class="form-group">
    <label for="description">Description</label>
    <textarea name="description" id="description" class="form-control"><?= isset($data['description']) ? $data['description'] : '' ?></textarea>
</div>
<div class="form-group">
    <button class="btn btn-primary">Ajouter l'évènement</button>
</div>