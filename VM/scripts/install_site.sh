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
nouvelle_chaine="DocumentRoot /var/www/html/Projet-InfraLogiciel/timefusion-web/public/"

# Vérifier si le répertoire existe
if [ -d "$repertoire/Projet-InfraLogiciel" ]; then
    echo "=> [1] - Le répertoire existe"
    cd "$repertoire/Projet-InfraLogiciel" || exit

    echo "=> [2] - Git pull"
    git pull
else
    cd "$repertoire" || exit

    echo "=> [1] - Git clone"
    git clone "https://github.com/viziergr/Projet-InfraLogiciel.git"

    cd Projet-InfraLogiciel || exit

    echo "=> [2] - Git checkout gregoire/testArchitecture"
    git checkout gregoire/testArchitecture

    echo "=> [3] - Configuration du git pull"
    git config pull.rebase false --global

    echo "=> [4] - Suppression des fichiers inutiles"
    # Suppression de tous les autres fichiers inutiles
    rm -r /var/www/html/Projet-InfraLogiciel/.vscode
    rm -r /var/www/html/Projet-InfraLogiciel/livrables
    rm -r /var/www/html/Projet-InfraLogiciel/timefusion-dekstop
    rm -r /var/www/html/Projet-InfraLogiciel/VM
    rm /var/www/html/Projet-InfraLogiciel/Configurations.txt
    rm /var/www/html/Projet-InfraLogiciel/ProjetInfraLog.drawio
    rm /var/www/html/Projet-InfraLogiciel/README.md
fi

echo "[5] - Modification de la configuration du site 000-default.conf"
# Vérifier si la chaîne de caractères à rechercher existe dans le fichier
if grep -q "$nouvelle_chaine" "$fichier_conf"; then
    # La chaîne existe, ne rien faire
    echo "=> Le chemin d'accès au site existe déjà."
else
    # La chaîne n'existe pas, la remplacer
    sed -i "s|$chaine_a_rechercher|$nouvelle_chaine|g" "$fichier_conf"
    echo "=> La chemin d'accès au site a été modifié."
fi

echo "=> [6] - Déplacement de /myadmin/"
    sudo mv /var/www/html/myadmin /var/www/html/Projet-InfraLogiciel/timefusion-web/public/

service apache2 reload
echo "END - Deplacement des fichiers"
