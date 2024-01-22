#!/bin/bash

echo "START - Automatisation de la sauvegarde avec cron"

echo "=> [1] Creation fichier temporaire"

touch tpm /home/vagrant


echo "=> [2] Ajout de la tâche sur crontab"

echo "* * * * * sh /vagrant/scripts/sav.sh" > /home/vagrant/tmp

crontab -u vagrant /home/vagrant/tmp

echo "=> [3] Suppression du fichier temporaire"

rm /home/vagrant/tmp