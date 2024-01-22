#!/bin/bash

IP=$(hostname -I | awk '{print $2}')

APT_OPT="-o Dpkg::Progress-Fancy="0" -q -y"
LOG_FILE="/vagrant/logs/install_proxy.log"
# Ajout du reverse proxy dans le fichier ci-dessous qui permet d'accéder au site
echo "START - install proxy" $IP
echo "=> [1] - Modification du fichier 000-default.conf"
sudo sed -i 's/<\/VirtualHost>//g' /etc/apache2/sites-available/000-default.conf
echo "ServerName TimeFusion-proxy
        # Activation du module de proxy
        ProxyRequests Off
        ProxyPreserveHost On

        <Proxy *>
         Order deny,allow
         Allow from all
        </Proxy>

        # Configuration du reverse proxy
        ProxyPass / http://192.168.56.80/
        ProxyPassReverse / http://192.168.56.80/
</VirtualHost>" >> /etc/apache2/sites-available/000-default.conf

echo "=> [2] - Activation des modules proxy et proxy_http sur le serveur web Apache"
# Activation des modules proxy et proxy_http sur le serveur web Apache
a2enmod proxy proxy_http

echo "=> [3] - Création du nouvel hôte pour le reverse proxy"
# Création du nouvel hôte pour le reverse proxy
sudo mkdir -p /etc/apache2/sites-available 
sudo touch /etc/apache2/sites-available/reverse-proxy.conf

echo "=> [4] - Activation du site reverse-proxy"
# Activation du reverse proxy
sudo a2ensite reverse-proxy 

echo "=> [5] - Service apache2 restart"
# Relancer apache2 pour mettre à jour le proxy
sudo systemctl reload apache2 
sudo service apache2 restart 

echo "END - reverse proxy installed"