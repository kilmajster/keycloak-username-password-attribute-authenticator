#!/usr/bin/env bash

pwd

rm -rf .gnupg
mkdir -m 0700 .gnupg
touch .gnupg/gpg.conf
chmod 600 .gnupg/gpg.conf
tail -n +4 /usr/share/gnupg2/gpg-conf.skel > .gnupg/gpg.conf

cd .gnupg

pwd

cat >keydetails <<EOF
    %echo Generating a basic OpenPGP key
    Key-Type: RSA
    Key-Length: 2048
    Subkey-Type: RSA
    Subkey-Length: 2048
    Name-Real: Łukasz Włódarczyk
    Name-Comment: kilmajster
    Name-Email: lukasz.createam@gmail.com
    Expire-Date: 0
    %no-ask-passphrase
    %no-protection
    %pubring pubring.kbx
    %secring trustdb.gpg
    # Do a commit here, so that we can later print "done" :-)
    %commit
    %echo done
EOF

gpg2 --verbose --batch --gen-key keydetails