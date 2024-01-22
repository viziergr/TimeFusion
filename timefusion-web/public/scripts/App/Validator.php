<?php

namespace TimeFusion\App;

class Validator {

    private $data;
    protected $errors = [];

    public function validates(array $data) {
        $this->errors = [];
        $this->data = $data;
    }

    public function validate(string $field, string $method, ...$parameters){
        if (!isset($this->data[$field])) {
            $this->errors[$field] = "Le champ $field n'est pas rempli";
        } else {
            call_user_func([$this, $method], $field, ...$parameters);
        }
    }

    public function minLength(string $field, int $length) {
        if (mb_strlen($this->data[$field]) < $length) {
            $this->errors[$field] = "Le champ doit avoir plus de $length caractères";
            return false;
        }
        return true;
    }

    public function date (string $field) {
        if (\DateTime::createFromFormat('Y-m-d', $this->data[$field]) === false) {
            $this->errors[$field] = "Le date ne semble pas valide";
            return false;
        }
        return true;
    }

    public function time (string $field) {
        if (\DateTime::createFromFormat('H:i', $this->data[$field]) === false) {
            $this->errors[$field] = "Le temps doit être une heure au format HH:MM";
            return false;            
        }
        return true;
    }

    public function beforeTime (string $startField, string $endField){
        if($this->time($startField) && $this->time($endField)){
            $start = \DateTime::createFromFormat('H:i', $this->data[$startField]);
            $end = \DateTime::createFromFormat('H:i', $this->data[$endField]);
            if ($start->getTimestamp() > $end->getTimestamp()) {
                $this->errors[$startField] = "Le temps de début doit être inférieur au temps de fin";
                return false;
            }
            return true;
        }
    }
}

?>