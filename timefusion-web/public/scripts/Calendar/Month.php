<?php

namespace TimeFusion\Calendar;

class Month {

    public $days = ['Lundi','Mardi','Mercredi','Jeudi','Vendredi','Samedi','Dimanche'];
    private $months = ['Janvier','Février','Mars','Avril','Mai','Juin','Juillet','Août','Septembre','Octobre','Novembre','Décembre'];
    public $month;
    public $year;
    
    /**
     * Constructeur Mois
     * @param int $month Un mois entre 1 et 12
     * @param int $year L'année
     * @throws \Exception
     */

    public function __construct(?int $month = null, ?int $year = null) 
    {
        if ($month === null || $month <1 ||  $month > 12) {
            $month = intval(date('m'));
        }
        if ($year === null) {
            $year = intval(date('Y'));
        }
        if ($month < 1|| $month > 12) {
            throw new \Exception("Le mois $month n'est pas valide");
        }
        if($year < 1970) {
            throw new \Exception("L'année $year n'est pas valide");
        }
        $this->month = $month;
        $this->year = $year;
    }

    public function __toString() : string {
        return $this->months[$this->month-1] .' '. $this->year;
    }

    public function getFirstDay (): \DateTime {
        return new \DateTime("{$this->year}-{$this->month}-01");
    }

    public function getWeeks (): int {
        $start = $this->getFirstDay();
        $end = (clone $start)->modify('+1 month -1 day');
        $weeks = intval($end->format('W'))-intval($start->format('W'))+1;
        if ($weeks < 0) {
            if ($start->format('N')==='6'|| $start->format('N')=== '7') {
                $weeks = 6;
            } else {
                $weeks = 5;
            }
        }
        return $weeks;
    }

    public function nextMonth (): Month {
        $month = $this->month+1;
        $year = $this->year;
        if ($month > 12) {
            $month = 1;
            $year +=1;
        }
        return new Month($month, $year);
    }

    public function previousMonth (): Month {
        $month = $this->month-1;
        $year = $this->year;
        if ($month < 1) {
            $month = 12;
            $year -=1;
        }
        return new Month($month, $year);
    }

    public function withinMonth (\DateTime $date): bool{
        return $this->getFirstDay()->format('Y-m') === $date->format('Y-m');
    }

}