<?php

namespace TimeFusion\Calendar;

class Events {

    private $mysqli;

    /**
     * Events constructor.
     *
     * @param \mysqli $mysqli The MySQLi object used for database connection.
     */
    public function __construct(\mysqli $mysqli) {
        $this->mysqli = $mysqli;
    }

    /**
     * Get events between two dates.
     *
     * @param \DateTime $start The start date.
     * @param \DateTime $end The end date.
     * @return array The array of events between the specified dates.
     */
    public function getEventsBetween (\DateTime $start, \DateTime $end, $usersId): array {
        // Convertit la liste d'ids en une chaîne pour l'utilisation dans la requête SQL
        $usersIdList = implode(',', array_map('intval', $usersId));
    
        $sql = "SELECT * FROM event 
                WHERE start_time BETWEEN '{$start->format('Y-m-d 00:00:00')}' AND '{$end->format('Y-m-d 23:59:59')}' 
                AND creator_id IN ($usersIdList)";
    
        // Exécute la requête SQL
        $result = $this->mysqli->query($sql);
    
        if (!$result) {
            // Gère les erreurs ici, si nécessaire
            echo "Error in query: " . $this->mysqli->error;
            return [];
        }
    
        // Récupère les données résultantes
        $eventsData = $result->fetch_all(MYSQLI_ASSOC);
    
        // Libère le résultat de la requête
        $result->free_result();
    
        return $eventsData;
    }
    
    
    
    /**
     * Get events between two dates grouped by day.
     *
     * @param \DateTime $start The start date.
     * @param \DateTime $end The end date.
     * @return array The array of events between the specified dates, grouped by day.
     */
    public function getEventsBetweenByDay (\DateTime $start, \DateTime $end, array $usersId): array {
        $events = $this->getEventsBetween($start, $end, $usersId);

        $days = [];

        foreach ($events as $event) {
                $date = explode(' ', $event['start_time'])[0];
                if (!isset($days[$date])) {
                    $days[$date] = [$event];
                } else {
                    $days[$date][] = $event;
                }
        }
        return $days;
    }

    /**
     * Finds an event by its ID.
     *
     * @param int $id The ID of the event to find.
     * @return array The event details as an associative array.
     * @throws \Exception If no event is found with the given ID.
     */
    public function find(int $id): Event {
        require 'Event.php';
        $result = $this->mysqli->query("SELECT * FROM event WHERE id = $id LIMIT 1")->fetch_assoc();
    
        if ($result === null) {
            throw new \Exception("Aucun évènement n'a été trouvé");
        }
    
        // Create an instance of Event using the static method
        $event = Event::createFromDbResult($result);
    
        return $event;
    }

    public function hydrate(Event $event, array $data, int $userId){
        $event->setTitle($data['name']);
        $event->setStart(\DateTime::createFromFormat('Y-m-d H:i',$data['date'] . ' ' . $data['start'])->format('Y-m-d H:i:s'));
        $event->setEnd(\DateTime::createFromFormat('Y-m-d H:i',$data['date'] . ' ' . $data['end'])->format('Y-m-d H:i:s'));
        $event->setDescription($data['description']);
        $event->setCreatorId($userId);
        return $event;
    }

    public function create(Event $event) {
        $sql = "INSERT INTO event (title, description, start_time, end_time, creator_id) VALUES (?, ?, ?, ?, ?)";
        $stmt = $this->mysqli->prepare($sql);
    
        if (!$stmt) {
            // Gérer l'erreur, par exemple, retourner false ou lever une exception
            return false;
        }
    
        // Extraire les valeurs des méthodes de l'objet Event
        $title = $event->getTitle();
        $description = $event->getDescription();
        $startTime = $event->getStartTime()->format('Y-m-d H:i:s');
        $endTime = $event->getEndTime()->format('Y-m-d H:i:s');
        $creatorId = $event->getCreatorId();
    
        // Bind parameters avec des variables
        $stmt->bind_param("sssss", $title, $description, $startTime, $endTime, $creatorId);
    
        // Exécuter la requête
        $result = $stmt->execute();
    
        // Fermer le statement
        $stmt->close();
    
        return $result;
    }
    

    public function update (Event $event) {
        $sql = "UPDATE event SET title = ?, description = ?, start_time = ?, end_time = ? WHERE id = ?";
        $stmt = $this->mysqli->prepare($sql);
        return $stmt->execute([
            $event->getTitle(),
            $event->getDescription(),
            $event->getStartTime()->format('Y-m-d H:i:s'),
            $event->getEndTime()->format('Y-m-d H:i:s'),
            $event->getId()
        ]);
    }
}

?>