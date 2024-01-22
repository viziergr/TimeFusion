#!/bin/bash

## déplace les fichiers du site

IP=$(hostname -I | awk '{print $2}')

APT_OPT="-o Dpkg::Progress-Fancy="0" -q -y"
LOG_FILE="/vagrant/logs/install_site.log"
DEBIAN_FRONTEND="noninteractive"

echo "START - Deplacement des fichiers - "$IP
# Définir le chemin du répertoire
repertoire="/var/www/html/"

# Chemin du fichier de configuration Apache
fichier_conf="/etc/apache2/sites-available/000-default.conf"

# Chaîne de caractères à rechercher
chaine_a_rechercher="DocumentRoot /var/www/html"

# Nouvelle chaîne de caractères
nouvelle_chaine="DocumentRoot /var/www/html/TimeFusion/timefusion-web/public/"

# Vérifier si le répertoire existe
if [ -d "$repertoire/TimeFusion" ]; then
    echo "=> [1] - Le répertoire existe"
    cd "$repertoire/TimeFusion" || exit

    echo "=> [2] - Git pull"
    git pull
else
    cd "$repertoire" || exit

    echo "=> [1] - Git clone"
    git clone "https://github.com/viziergr/TimeFusion.git"

    cd TimeFusion || exit

    echo "=> [2] - Configuration du git pull"
    git config pull.rebase false --global

    echo "=> [3] - Suppression des fichiers inutiles"
    # Suppression de tous les autres fichiers inutiles
    rm -r /var/www/html/TimeFusion/.vscode
    rm -r /var/www/html/TimeFusion/livrables
    rm -r /var/www/html/TimeFusion/timefusion-dekstop
    rm -r /var/www/html/TimeFusion/VM
    rm /var/www/html/TimeFusion/Configurations.txt
    rm /var/www/html/TimeFusion/ProjetInfraLog.drawio
    rm /var/www/html/TimeFusion/README.md
fi

echo "=> [4] - Modification de la configuration du site 000-default.conf"
# Vérifier si la chaîne de caractères à rechercher existe dans le fichier
if grep -q "$nouvelle_chaine" "$fichier_conf"; then
    # La chaîne existe, ne rien faire
    echo "=> Le chemin d'accès au site existe déjà."
else
    # La chaîne n'existe pas, la remplacer
    sed -i "s|$chaine_a_rechercher|$nouvelle_chaine|g" "$fichier_conf"
    echo "=> La chemin d'accès au site a été modifié."
fi

echo "=> [5] - Déplacement de /myadmin/"
sudo mv /var/www/html/myadmin /var/www/html/TimeFusion/timefusion-web/public/

echo "=> [6] - Modification de la configuration de myadmin"
sed -i "s/localhost/192.168.56.81/g" /var/www/html/TimeFusion/timefusion-web/public/myadmin/config.inc.php

service apache2 reload
echo "END - Deplacement des fichiers"
