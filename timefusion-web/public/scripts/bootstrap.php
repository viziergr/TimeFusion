<?php
error_reporting(E_ALL);
ini_set('display_errors', 'On');
ini_set('display_startup_errors', 'On');

function e404(){
    include __DIR__ . '/../pages/404.php';
    exit();
}

function connectDB() {

    $infoBdd = [
        'server' => '192.168.56.81',
        'login' => 'root',
        'password' => '',
        'db_name' => 'TimeFusion',
    ];

    $mysqli = new mysqli(
        $infoBdd['server'], $infoBdd['login'],
        $infoBdd['password'], $infoBdd['db_name']
    );
    
    // Vérifier la connexion à la base de données
    if ($mysqli->connect_errno) {
        exit('Problème de connexion à la BDD');
    }

    return $mysqli; // Retourne l'objet MySQLi pour être utilisé dans d'autres parties du code
}

function dd(...$vars){
    foreach ($vars as $var) {
        echo '<pre>';
        print_r($var);
        echo '</pre>';
    }
}

function h(string $value): string {
    if($value === null) return 'null';
    return htmlentities($value);
}

function render(string $view, $parameters = []) {
    extract($parameters);
    include __DIR__ . "/../includes/" . $view .".php";
}

function sess_exists(){
    session_start();
    // Redirige vers la page de connexion si l'utilisateur n'est pas connecté
    if (!isset($_SESSION['compte'])) {
        header("Location: ../Connexion.php");
        exit();
    }
}

function convertirEnArray($variable) {
    // Vérifier si la variable est déjà un tableau
    if (is_array($variable)) {
        // La variable est déjà un tableau, rien à faire
        return $variable;
    } else {
        // La variable n'est pas un tableau, créer un tableau avec la variable
        return array($variable);
    }
}

function estCouleurValide($couleur) {
    // Expression régulière pour vérifier le format hexadécimal de la couleur
    $pattern = '/^#[0-9A-Fa-f]{6}$/';

    // Vérification avec la fonction preg_match
    return preg_match($pattern, $couleur) === 1;
}

?>
