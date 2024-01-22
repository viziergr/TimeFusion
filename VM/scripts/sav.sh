#!/bin/bash

#Informations de la BDD à sauvegarder

DB_USER="root"
DB_PASS="root"
DB_NAME="TimeFusion"



# Répertoire de sauvegarde local
LOCAL_BACKUP_DIR="/home/vagrant/sav"

#Temps 
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

#Création du répertoire de sauvegarde sur la VM bdd s'il n'existe pas
if [ ! -d "$LOCAL_BACKUP_DIR" ]; then
    mkdir -p "$LOCAL_BACKUP_DIR"
    chown vagrant $SSH_DIR
    chmod 600 $SSH_DIR
fi


#Nom du fichier de backup

BACKUP_FILE="db_backup"${TIMESTAMP}.sql

echo "$BACKUP_FILE"

# Répertoire de sauvegarde distant
REMOTE_BACKUP_DIR="vagrant@192.168.56.83:/home/vagrant/sav"

# Commande de sauvegarde avec mysqldump, exportation de la bdd vers un fichier .sql
mysqldump -u $DB_USER -p$DB_PASS $DB_NAME > /home/vagrant/sav/"$BACKUP_FILE"

    chown vagrant "$LOCAL_BACKUP_DIR/$BACKUP_FILE"
    chmod 777 "$LOCAL_BACKUP_DIR/$BACKUP_FILE"

scp -o StrictHostKeyChecking=no -i /home/vagrant/.ssh/id_rsa /home/vagrant/sav/"$BACKUP_FILE" vagrant@192.168.56.83:/home/vagrant/sav




