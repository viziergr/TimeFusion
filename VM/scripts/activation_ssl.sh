#!/bin/bash

LOG_FILE="/vagrant/logs/activation_ssl.log"

echo "START - activation SSL"

echo "=> [1] - Activation du certificat SSL"
# Activation du certificat SSL

openssl x509 -req -CA rootCA.crt -CAkey rootCA.key -in domain.csr -out domain.crt -days 365 -CAcreateserial -extfile domain.ext -passin pass:TimeFusion

echo "=> [2] - Modification de 000-default.conf"
# Modification de 000-default.conf
rm /etc/apache2/sites-available/000-default.conf
echo "<VirtualHost *:443>

        ServerAdmin webmaster@localhost
        DocumentRoot /var/www/html
        ErrorLog ${APACHE_LOG_DIR}/error.log
        CustomLog ${APACHE_LOG_DIR}/access.log combined

        ServerName TimeFusion.reverse-proxy
        ProxyRequests Off
        ProxyPreserveHost On

        <Proxy *>
         Order deny,allow
         Allow from all
        </Proxy>

        ProxyPass / http://192.168.56.80/
        ProxyPassReverse / http://192.168.56.80/

        SSLEngine on
        SSLCertificateFile      /root/rootCA.crt
        SSLCertificateKeyFile   /root/rootCA.key

    </VirtualHost>
    <VirtualHost *:80>

        ServerAdmin webmaster@localhost
        DocumentRoot /var/www/html
        ErrorLog ${APACHE_LOG_DIR}/error.log
        CustomLog ${APACHE_LOG_DIR}/access.log combined

        ServerName TimeFusion.reverse-proxy
        ProxyRequests Off
        ProxyPreserveHost On

        <Proxy *>
         Order deny,allow
         Allow from all
        </Proxy>

        ProxyPass / http://192.168.56.80/
        ProxyPassReverse / http://192.168.56.80/

    </VirtualHost>" >> /etc/apache2/sites-available/000-default.conf

echo "=> [3] - Activation du site 000-default.conf"
# Activation du site 000-default.conf
a2ensite 000-default.conf -p TimeFusion

echo "=> [4] - Activation du module SSL"
# Activation du module SSL
a2enmod ssl

echo "=> [5] - Relancer apache2 pour mettre à jour le proxy"
# Relancer apache2 pour mettre à jour le proxy
systemctl reload apache2
service apache2 restart