<?php

namespace TimeFusion\Team;

if (!class_exists('TimeFusion\Team\Team')) {
    include __DIR__ . '/Team.php';
}
if (!class_exists('TimeFusion\Team\User')) {
    include __DIR__ . '/User.php';
}
if (!class_exists('TimeFusion\Team\Users')) {
    include __DIR__ . '/Users.php';
}

class Teams
{
    private $mysqli;

    public function __construct(\mysqli $mysqli) {
        $this->mysqli = $mysqli;
    }

    // Récupérer les équipes d'un utilisateur
    // Récupérer les équipes d'un utilisateur avec leurs membres
    public function getUserTeams($userId)
    {
        $result = $this->mysqli->query("SELECT team.*, team_membership.user_id
                                FROM team
                                JOIN team_membership ON team.id = team_membership.team_id
                                WHERE team_membership.user_id = '$userId'");

        if (!$result) {
            die('Erreur SQL : ' . $this->mysqli->error);
        }

        $teams = [];
    
        while ($row = $result->fetch_assoc()) {
            $team = new Team($row['name']);
            $team->setColor($row['color']);
            $team->setId($row['id']); // Assurez-vous d'avoir une méthode setId dans votre classe Team
            // Ajoutez d'autres propriétés au besoin
    
            // Récupérer les membres de l'équipe avec leurs informations complètes depuis la table User
            $membersResult = $this->mysqli->query("SELECT u.id as user_id, u.first_name, u.last_name, u.email, u.password, u.year, tm.role
                                                   FROM team_membership tm
                                                   JOIN user u ON tm.user_id = u.id
                                                   WHERE tm.team_id = '{$row['id']}'");
    
            $members = [];
            while ($memberRow = $membersResult->fetch_assoc()) {
                // Créer un objet User avec les informations complètes
                $user = new User($memberRow['user_id'], $memberRow['first_name'], $memberRow['last_name'], $memberRow['email'], $memberRow['password'], $memberRow['year']);
                $members[] = ['user' => $user, 'role' => $memberRow['role']];
            }
    
            // Ajouter les membres à l'équipe
            $team->setMembers($members);
    
            // Ajouter l'équipe à la liste des équipes
            $teams[] = $team;
        }
    
        return $teams;
    }
    
    // Fonction pour obtenir une couleur pastel aléatoire
    public function getRandomPastelColor() {
        static $pastelColors = null;
    
        if ($pastelColors === null) {
            // Liste prédéfinie de couleurs pastel
            $pastelColors = array(
                '#77dd77', '#99ddff', '#ffcccb', '#ffddca', '#c0c0c0', '#ffb6c1', '#d2b48c',
                '#b19cd9', '#ffdb58', '#b0e0e6', '#a0d6b4', '#f0e68c', '#c2b280', '#dda0dd'
            );
    
            // Mélanger la liste des couleurs
            shuffle($pastelColors);
        }
    
        // Retourner et retirer la première couleur de la liste
        return array_shift($pastelColors);
    }

    public function getMembersByTeamId($team_id)
    {
        $result = $this->mysqli->query("SELECT user_id FROM team_membership WHERE team_id = $team_id");
        if ($result === false) {
            throw new \Exception("Erreur lors de l'exécution de la requête : " . $this->mysqli->error);
        }
        $userIds = array(); // Initialise un tableau vide pour stocker les user_ids
        // Boucle sur les résultats pour extraire les user_ids
        while ($row = $result->fetch_assoc()) {
            $userIds[] = $row['user_id'];
        }
        // Vérifie s'il n'y a aucun membre trouvé
        if (empty($userIds)) {
            throw new \Exception("Aucun membre n'a été trouvé");
        }
        return $userIds;
    }

    public function hydrate(Team $team, array $data){
        $name = $data['name'];
        $color = $data['color'];
    
        // Vérification de l'existence d'une équipe du même nom
        if ($this->teamNameExists($name)) {
            return null; // Équipe du même nom déjà existante
        }
    
        // Hydratation de l'équipe
        $team->setName($name);
        $team->setColor($color);
    
        return $team;
    }
    
    // Méthode pour vérifier l'existence d'une équipe du même nom
    private function teamNameExists($name) {
        $sql = "SELECT COUNT(*) as count FROM team WHERE name = '$name'";
        
        // Exécutez la requête SQL
        $result = $this->mysqli->query($sql);
    
        if ($result) {
            // Récupérez la première ligne du résultat
            $row = $result->fetch_assoc();
            // Retournez vrai si le nombre est supérieur à 0
            return $row['count'] > 0;
        }
        // Retournez faux en cas d'erreur d'exécution de la requête
        return false;
    }
    
    public function create(Team $team, $userId) {
        try {
            if ($userId == null) {
                throw new \Exception("L'identifiant de l'utilisateur est manquant");
            }
    
            $this->mysqli->begin_transaction();
    
            // Insertion de l'équipe
            $sqlInsertTeam = "INSERT INTO team (name, color, description) VALUES (?, ?, ?)";
            $stmtInsertTeam = $this->mysqli->prepare($sqlInsertTeam);
            if (!$stmtInsertTeam) {
                throw new \Exception("Erreur lors de la préparation de la requête d'insertion d'équipe");
            }
    
            $name = $team->getName();
            $color = $team->getColor();
            $description = $team->getDescription() ?? "Aucune description";
    
            $stmtInsertTeam->bind_param("sss", $name, $color, $description);
            $stmtInsertTeam->execute();
            $stmtInsertTeam->close();
    
            // Récupération de l'ID de l'équipe nouvellement créée
            $sqlSelectTeamId = "SELECT id FROM team WHERE name = ? LIMIT 1";
            $stmtSelectTeamId = $this->mysqli->prepare($sqlSelectTeamId);
            if (!$stmtSelectTeamId) {
                throw new \Exception("Erreur lors de la préparation de la requête de sélection de l'ID de l'équipe");
            }
    
            $stmtSelectTeamId->bind_param("s", $name);
            $stmtSelectTeamId->execute();
            $resultSelectTeamId = $stmtSelectTeamId->get_result();
    
            if (!$resultSelectTeamId) {
                throw new \Exception("Erreur lors de l'exécution de la requête de sélection de l'ID de l'équipe");
            }
    
            $row = $resultSelectTeamId->fetch_assoc();
            $teamId = $row['id'];
    
            $stmtSelectTeamId->close();
    
            // Insertion de l'appartenance à l'équipe
            $sqlInsertTeamMembership = "INSERT INTO team_membership (team_id, user_id, role) VALUES (?, ?, ?)";
            $stmtInsertTeamMembership = $this->mysqli->prepare($sqlInsertTeamMembership);
    
            if (!$stmtInsertTeamMembership) {
                throw new \Exception("Erreur lors de la préparation de la requête d'insertion de l'appartenance à l'équipe");
            }
    
            $role = 'Leader';
    
            $stmtInsertTeamMembership->bind_param("iss", $teamId, $userId, $role);
            $stmtInsertTeamMembership->execute();
            $stmtInsertTeamMembership->close();
    
            $this->mysqli->commit();
        } catch (\Exception $e) {
            // En cas d'erreur, annuler la transaction et gérer l'\Exception
            $this->mysqli->rollback();
            dd('Erreur : ' . $e->getMessage());
            return false;
        }
    
        return true;
    }

    public function getTeamObjectFromDb($team_id)
    {
        $result = $this->mysqli->query("SELECT * FROM team WHERE id = $team_id");
        if ($result === false) {
            throw new \Exception("Erreur lors de l'exécution de la requête : " . $this->mysqli->error);
        }
        $team = $result->fetch_assoc();

        $membersId = $this->getMembersByTeamId($team_id);

        foreach ($membersId as $memberId) {
            $members[] = (new Users($this->mysqli))->getUserById($memberId);
        }

        $team_data = array(
            'id' => $team['id'],
            'name' => $team['name'],
            'color' => $team['color'],
            'description' => $team['description'],
            'members' => $members
        );

        $team = (new Team())->createFromDbResult($team_data);

        return $team;
    }

    public function getRoleById($user_id, $team_id)
    {
        $result = $this->mysqli->query("SELECT role FROM team_membership WHERE user_id = $user_id AND team_id = $team_id");
        if ($result === false) {
            throw new \Exception("Erreur lors de l'exécution de la requête : " . $this->mysqli->error);
        }
        $role = $result->fetch_assoc();
        return $role['role'];
    }

    public function hasRights($userRole, $memberRole)
    {
        $permissions = false;
        switch($userRole){
            case 'Leader':
                switch($memberRole){
                    case 'Co-leader':
                        $permissions = true;
                        break;
                    case 'Elder':
                        $permissions = true;
                        break;
                    case 'Member':
                        $permissions = true;
                        break;
                }
                break;
            case 'Co-leader':
                switch($memberRole){
                    case 'Elder':
                        $permissions = true;
                        break;
                    case 'Member':
                        $permissions = true;
                        break;
                }
                break;
            case 'Elder':
                switch($memberRole){
                    case 'Member':
                        $permissions = true;
                        break;
                }
                break;
        }
        return $permissions;
    }

    public function promote($team_id, $user_id){
        $role = $this->getRoleById($user_id, $team_id);
        switch($role){
            case 'Member':
                $role = 'Elder';
                break;
            case 'Elder':
                $role = 'Co-leader';
                break;
            case 'Co-leader':
                $role = 'Leader';
                $leader_id = $this->getCurrentLeaderId($team_id);
                $this->mysqli->query("UPDATE team_membership SET role = 'Co-leader' WHERE user_id = $leader_id AND team_id = $team_id");
                break;
        }
        $this->mysqli->query("UPDATE team_membership SET role = '$role' WHERE user_id = $user_id AND team_id = $team_id");
    }

    public function relegate($team_id, $user_id){
        $role = $this->getRoleById($user_id, $team_id);
        switch($role){
            case 'Member':
                $this->removeMember($team_id, $user_id);
                break;
            case 'Elder':
                $role = 'Member';
                break;
            case 'Co-leader':
                $role = 'Elder';
                break;
            case 'Leader':
                $role = 'Co-leader';
                break;
        }
        $this->mysqli->query("UPDATE team_membership SET role = '$role' WHERE user_id = $user_id AND team_id = $team_id");
    }

    private function getCurrentLeaderId($team_id) {
        $result = $this->mysqli->query("SELECT user_id FROM team_membership WHERE team_id = $team_id AND role = 'Leader' LIMIT 1");
        $row = $result->fetch_assoc();
        return ($row) ? $row['user_id'] : null;
    }

    public function removeMember($team_id, $user_id) {
        // Vérifier si l'utilisateur est le leader de l'équipe
        $isLeader = $this->getRoleById($user_id, $team_id) === 'Leader';
    
        if ($isLeader) {
            throw new \Exception("Vous ne pouvez pas quitter l'équipe en tant que seul leader. Promouvez un autre membre au poste de leader avant de partir.");
        }
    
        // Retirer le membre de l'équipe
        $this->mysqli->query("DELETE FROM team_membership WHERE user_id = $user_id AND team_id = $team_id");
    }
        
}
