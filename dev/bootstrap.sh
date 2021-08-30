#!/bin/bash
#
# Based on the contents available at https://rjzaworski.com/2018/01/keeping-git-hooks-in-sync

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$DIR/.."

log() {
  echo "$(basename ${BASH_SOURCE[0]}): $@"
}

install_hooks() {
  rm -rf ./.git/hooks
  ln -s ../dev/hooks ./.git/hooks
}

log 're/configuring hooks...'
install_hooks
