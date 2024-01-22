<?php
$password = "password";

$hashedPassword = crypt($password, '$2a$12$' . bin2hex(random_bytes(22)));

echo "Mot de passe original : " . $password . "\n";
echo "Hash Bcrypt : " . $hashedPassword . "\n";

$enteredPassword = "password";
$hashedPasswordFromJava = "$2a$12$9cnM6FmT3QIN2NWe4nFDVe7t5t29oVGnFo.5QDOq85.VhuUB0aRC.";

if (password_verify($enteredPassword, $hashedPassword)) {
    echo "Le mot de passe est correct.\n";
} else {
    echo "Le mot de passe est incorrect.\n";
}
?>
