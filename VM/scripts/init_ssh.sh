#!/bin/bash

SSH_DIR="/home/vagrant/.ssh"
SSH_PRIVATE_KEY="$SSH_DIR/id_rsa"
SSH_PUBLIC_KEY="$SSH_PRIVATE_KEY.pub"

echo "START - Initialitation du SSH"


echo "=> [1] - Creation de cles SSH"
# Générez une nouvelle paire de clés SSH si elles n'existent pas déjà
if [ ! -f "$SSH_PRIVATE_KEY" ] || [ ! -f "$SSH_PUBLIC_KEY" ]; then
    ssh-keygen -t rsa -b 2048 -f "$SSH_PRIVATE_KEY" -N ""
    chown vagrant "$SSH_PRIVATE_KEY"
    chmod 700 "$SSH_PRIVATE_KEY"
    cp "$SSH_PUBLIC_KEY" /vagrant
    echo "Clés SSH générées avec succès."
else
    echo "Les clés SSH existent déjà. Aucune nouvelle clé n'a été générée."
fi

echo "END - Initialitation du SSH"
