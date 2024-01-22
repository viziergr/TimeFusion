#!/bin/bash

## Installer les tables

IP=$(hostname -I | awk {print $2})

APT_OPT="-o Dpkg::Progress-Fancy="0" -q -y"
LOG_FILE="/vagrant/logs/install_table.log"
DEBIAN_FRONTEND="noninteractive"
REP = "/var/www/html/git/Projet-InfraLogiciel/sql"

echo "START - Création des tables - "$IP

mysql -e "CREATE DATABASE TimeFusion;"

echo "START - Table user"
mysql TimeFusion < /var/www/html/git/Projet-InfraLogiciel/sql/user.sql
echo "END - Table user"

echo "START - Table team"
mysql TimeFusion < /var/www/html/git/Projet-InfraLogiciel/sql/team.sql
echo "END - Table team"

echo "START - Table team_membership"
mysql TimeFusion < /var/www/html/git/Projet-InfraLogiciel/sql/team_membership.sql
echo "END - Table team_membership"

echo "START - Table event"
mysql TimeFusion < /var/www/html/git/Projet-InfraLogiciel/sql/event.sql
echo "END - Table event"

echo "START - Table event_participant"
mysql TimeFusion < /var/www/html/git/Projet-InfraLogiciel/sql/event_participant.sql
echo "END - Table event_participant"

echo "START - Table requests"
mysql TimeFusion < /var/www/html/git/Projet-InfraLogiciel/sql/requests.sql
echo "END - Table requests"

service mariadb restart

echo "END - Création des tables"
