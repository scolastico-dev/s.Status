#!/bin/bash

if [ "$EUID" -ne 0 ]
  then echo "Please run as root"
  exit
fi

cd "$(dirname "$0")"

if [ ! -f bin/act ]; then
  cd .github/scripts
  curl -sL https://raw.githubusercontent.com/nektos/act/master/install.sh -o install.sh
  chmod +x install.sh
  ./install.sh
  rm install.sh
  cd ../..
fi

.github/scripts/bin/act
