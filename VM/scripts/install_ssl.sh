#!/bin/bash

IP=$(hostname -I | awk '{print $2}')

APT_OPT="-o Dpkg::Progress-Fancy="0" -q -y"
LOG_FILE="/vagrant/logs/install_ssl.log"

echo "START - install SSL" $IP
cd /root/
echo "=> [1] - Création de la clé privée"
openssl genrsa -passout pass:TimeFusion -des3 -out domain.key 2048 

echo "=> [2] - Création d'une requête de signature de certificat (CSR)"
openssl req -key domain.key -new -out domain.csr -passin pass:TimeFusion -subj "/C=FR/ST=Maine-et-Loire/L=Angers/O=ESEO/CN=TimeFusion"

echo "=> [3] - Création du certificat auto-signé"
openssl req -key domain.key -new -x509 -days 365 -out domain.crt -passin pass:TimeFusion -subj "/C=FR/ST=Maine-et-Loire/L=Angers/O=ESEO/CN=TimeFusion"

echo "=> [4] - Création du certificat d'autorité de certification auto-signé"
openssl req -x509 -sha256 -days 1825 -newkey rsa:2048 -keyout rootCA.key -out rootCA.crt -passout pass:TimeFusion -subj "/C=FR/ST=Maine-et-Loire/L=Angers/O=ESEO/CN=TimeFusion"

echo "=> [5] - Création du fichier /root/domain.ext, à modifier avec l'ip de la machine"
echo "authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
subjectAltName = @alt_names
[alt_names]
DNS.1 = adresse_ip" > domain.ext

echo "=> [6] - Modification des permissions pour pouvoir activer le certificat SSL"
# Modification des permissions pour pouvoir activer le certificat SSL
sudo chmod 777 /vagrant/scripts/activation.sh