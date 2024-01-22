#!/bin/bash

## Installer les tables

IP=$(hostname -I | awk {print $2})

APT_OPT="-o Dpkg::Progress-Fancy="0" -q -y"
LOG_FILE="/vagrant/logs/install_table.log"
DEBIAN_FRONTEND="noninteractive"
REP = "/var/www/html/git/TimeFusion/sql"

echo "START - Création des tables - "$IP

mysql -e "CREATE DATABASE TimeFusion;"

echo "[1] - Table user"
mysql TimeFusion < /var/www/html/git/TimeFusion/sql/user.sql

echo "[2] - Table team"
mysql TimeFusion < /var/www/html/git/TimeFusion/sql/team.sql

echo "[3] - Table team_membership"
mysql TimeFusion < /var/www/html/git/TimeFusion/sql/team_membership.sql

echo "[4] - Table event"
mysql TimeFusion < /var/www/html/git/TimeFusion/sql/event.sql

echo "[5] - Table event_participant"
mysql TimeFusion < /var/www/html/git/TimeFusion/sql/event_participant.sql

echo "[6] - Table requests"
mysql TimeFusion < /var/www/html/git/TimeFusion/sql/requests.sql

service mariadb restart

echo "END - Création des tables"
