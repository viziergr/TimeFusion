#!/bin/bash

## déplace les fichiers du site

IP=$(hostname -I | awk '{print $2}')

APT_OPT="-o Dpkg::Progress-Fancy="0" -q -y"
LOG_FILE="/vagrant/logs/recup_sql.log"
DEBIAN_FRONTEND="noninteractive"

echo "START - Deplacement des fichiers - "$IP

cd /var/www/html/
mkdir git
cd git
git clone https://github.com/viziergr/TimeFusion.git
cd TimeFusion
git checkout gregoire/testArchitecture
git config pull.rebase false --global

mkdir /var/www/html/siteweb/

rm -r /var/www/html/git/TimeFusion/.vscode
rm -r /var/www/html/git/TimeFusion/livrables
rm -r /var/www/html/git/TimeFusion/timefusion-dekstop
rm -r /var/www/html/git/TimeFusion/timefusion-web
rm -r /var/www/html/git/TimeFusion/VM
rm /var/www/html/git/TimeFusion/Configurations.txt
rm /var/www/html/git/TimeFusion/ProjetInfraLog.drawio
rm /var/www/html/git/TimeFusion/README.md

echo "END - Deplacement des fichiers"
