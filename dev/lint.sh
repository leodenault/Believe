#!/bin/bash

BIN_LOCATION=/usr/local/bin

echo $@

exit

if [ ! -f $BIN_LOCATION/ktlint ]; then
    echo "ktlint not found under $BIN_LOCATION. Installing now."
    curl -sSLO https://github.com/pinterest/ktlint/releases/download/0.42.1/ktlint &&
      chmod a+x ktlint &&
      sudo mv ktlint /usr/local/bin/
fi
