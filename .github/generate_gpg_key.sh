#!/usr/bin/env bash

cat >keydetails <<EOF
    %echo Generating a basic OpenPGP key
    Key-Type: RSA
    Key-Length: 2048
    Subkey-Type: RSA
    Subkey-Length: 2048
    Name-Real: Łukasz Włódarczyk
    Name-Comment: github
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

gpg2 --list-keys