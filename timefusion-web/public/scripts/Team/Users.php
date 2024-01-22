<?php

namespace TimeFusion\Team;

if (!class_exists('TimeFusion\Team\User')) {
    include __DIR__ . '/User.php';
}

class Users
{
    private $mysqli;

    public function __construct(\mysqli $mysqli) {
        $this->mysqli = $mysqli;
    }

    // Récupérer les équipes d'un utilisateur
    // Récupérer les équipes d'un utilisateur avec leurs membres
    // Dans la classe Requests

    public function getEveryOtherUsers($userId) {
        // Prépare la requête SQL pour récupérer tous les utilisateurs sauf celui avec l'ID spécifié
        $sql = "SELECT id, first_name, last_name, email, year FROM user WHERE id != ?";
        $stmt = $this->mysqli->prepare($sql);
        $stmt->bind_param("i", $userId);
        $stmt->execute();

        // Récupère le résultat
        $result = $stmt->get_result();

        // Stocke les utilisateurs dans un tableau
        $users = [];
        while ($row = $result->fetch_assoc()) {
            $users[] = new User($row['id'], $row['first_name'], $row['last_name'], $row['email'], null, $row['year']);
        }

        // Ferme le statement
        $stmt->close();

        return $users;
    }

    public function getUserById($userId) {
        // Prépare la requête SQL pour récupérer les informations d'un utilisateur par son ID
        $sql = "SELECT id, first_name, last_name, email, password, year FROM user WHERE id = ?";
        $stmt = $this->mysqli->prepare($sql);
        $stmt->bind_param("i", $userId);
        $stmt->execute();

        // Récupère le résultat
        $result = $stmt->get_result();

        // Vérifie s'il y a une ligne de résultat
        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $user = new User($row['id'], $row['first_name'], $row['last_name'], $row['email'], $row['password'], $row['year']);
        } else {
            $user = null; // Aucun utilisateur trouvé
        }

        // Ferme le statement
        $stmt->close();

        return $user;
    }

}
