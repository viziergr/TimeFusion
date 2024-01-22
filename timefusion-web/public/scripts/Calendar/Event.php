<?php

namespace TimeFusion\Calendar;

class Event {

    private $id;
    private $start_time;
    private $end_time;
    private $title;
    private $description;
    private $location;
    private $is_private;
    private $creator_id;

    public function __construct(
        $id = null,
        $start_time = null,
        $end_time = null,
        $title = null,
        $description = null,
        $location = null,
        $is_private = null,
        $creator_id = null
    ) {
        $this->id = $id;
        $this->start_time = $start_time;
        $this->end_time = $end_time;
        $this->title = $title;
        $this->description = $description;
        $this->location = $location;
        $this->is_private = $is_private;
        $this->creator_id = $creator_id;
    }

    public static function createFromDbResult(array $result): Event {
        return new self(
            $result['id'],
            $result['start_time'],
            $result['end_time'],
            $result['title'],
            $result['description'],
            $result['location'],
            $result['is_private'],
            $result['creator_id']
        );
    }

    public function getId(): int {
        return $this->id;
    }

    public function getStartTime(): \DateTime {
        return new \DateTime($this->start_time);
    }

    public function getEndTime(): \DateTime {
        return new \DateTime($this->end_time);
    }

    public function getTitle(): string {
        return $this->title;
    }

    public function getDescription(): string {
        return $this->description;
    }

    public function getCreatorId(): int {
        return $this->creator_id;
    }

    public function __toString() {
        return sprintf($this->id, $this->start_time, $this->end_time, $this->title, $this->description);
    }

    public function setTitle(string $title) {
        $this->title = $title;
    }

    public function setDate(string $date) {
        $this->date = $date;
    }

    public function setStart(string $start) {
        $this->start_time = $start;
    }

    public function setEnd(string $end) {
        $this->end_time = $end;
    }

    public function setDescription(string $description) {
        $this->description = $description;
    }

    public function setCreatorId(int $creator_id) {
        $this->creator_id = $creator_id;
    }
}