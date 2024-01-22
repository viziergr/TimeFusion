<?php

namespace TimeFusion\Team;

class User
{
    private $id;
    private $firstName;
    private $lastName;
    private $email;
    private $password;
    private $year;

    public function __construct($id=null, $firstName=null, $lastName=null, $email=null, $password=null, $year=null)
    {
        $this->id = $id;
        $this->firstName = $firstName;
        $this->lastName = $lastName;
        $this->email = $email;
        $this->password = $password;
        $this->year = $year;
    }

    // Ajoutez des méthodes d'accès ou d'autres méthodes utiles selon vos besoins

    public function getId()
    {
        return $this->id;
    }

    public function getFirstName()
    {
        return $this->firstName;
    }

    public function getLastName()
    {
        return $this->lastName;
    }

    public function getEmail()
    {
        return $this->email;
    }

    public function getPassword()
    {
        return $this->password;
    }

    public function getYear()
    {
        return $this->year;
    }

    public function getFullName()
    {
        return $this->firstName . ' ' . $this->lastName;
    }
}
