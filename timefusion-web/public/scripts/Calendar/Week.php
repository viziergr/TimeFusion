<?php

namespace TimeFusion\Calendar;

class Week {

    public $days = ['Lundi','Mardi','Mercredi','Jeudi','Vendredi','Samedi','Dimanche'];

    public $week;
    public $year;

    /**
     * Constructeur Semaine
     * @param int $week Une semaine entre 1 et 52
     * @param int $year L'année
     * @throws \Exception
     */

    public function __construct(?int $week = null, ?int $year = null) 
    {
        if ($week === null || $week < 1 || $week > 52) {
            $week = intval(date('W'));
        }
        if ($year === null) {
            $year = intval(date('Y'));
        }
        if ($week < 1 || $week > 52) {
            throw new \Exception("La semaine $week n'est pas valide");
        }
        if ($year < 1970) {
            throw new \Exception("L'année $year n'est pas valide");
        }
        $this->week = $week;
        $this->year = $year;
    }

    public function __toString() : string {
        return "Semaine {$this->week} de l'année {$this->year}";
    }

    public function getFirstDay (): \DateTime {
        $start = new \DateTime();
        $start->setISODate($this->year, $this->week, 1);
        return $start;
    }

    public function getLastDay (): \DateTime {
        $end = new \DateTime();
        $end->setISODate($this->year, $this->week, 7);
        return $end;
    }

    public function nextWeek (): Week {
        $week = $this->week + 1;
        $year = $this->year;
        if ($week > 52) {
            $week = 1;
            $year += 1;
        }
        return new Week($week, $year);
    }

    public function previousWeek (): Week {
        $week = $this->week - 1;
        $year = $this->year;
        if ($week < 1) {
            $week = 52;
            $year -= 1;
        }
        return new Week($week, $year);
    }

    public function withinWeek (\DateTime $date): bool {
        $start = $this->getFirstDay();
        $end = $this->getLastDay();
        return $date >= $start && $date <= $end;
    }

}
