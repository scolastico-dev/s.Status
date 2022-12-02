#!/bin/bash

if [ ! -f bin/act ]; then
  cd .fleet
  curl -sL https://raw.githubusercontent.com/nektos/act/master/install.sh -o install.sh
  chmod +x install.sh
  echo "Acquiring sudo to be able to install act..."
  sudo ./install.sh
  rm install.sh
  cd ..
fi

echo "Acquiring sudo to be able to run docker..."
sudo .fleet/bin/act
