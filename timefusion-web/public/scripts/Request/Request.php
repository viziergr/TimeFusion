<?php

namespace TimeFusion\Request;

class Request
{
    private $teamId;

    private $team_name;
    private $userId;
    private $status;
    private $id;

    private $type;

    public function __construct(int $id=null, int $teamId=null, string $team_name, int $userId=null, string $status=null, string $type='team')
    {
        $this->id = $id;
        $this->teamId = $teamId;
        $this->team_name = $team_name;
        $this->userId = $userId;
        $this->status = $status;
        $this->type = $type;
    }
    
    public function getId()
    {
        return $this->id;
    }

    public function getTeamId()
    {
        return $this->teamId;
    }

    public function getUserId()
    {
        return $this->userId;
    }

    public function getStatus()
    {
        return $this->status;
    }

    public function getTeamName()
    {
        return $this->team_name;
    }

    public function getType()
    {
        return $this->type;
    }
}
