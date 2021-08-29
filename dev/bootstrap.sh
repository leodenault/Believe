#!/bin/bash
#
# Based on the contents available at https://rjzaworski.com/2018/01/keeping-git-hooks-in-sync

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$DIR/.."

log() {
  echo "$(basename ${BASH_SOURCE[0]}): $@"
}

install_hooks() {
  git diff --quiet $1 $2 './dev/hooks' 2> /dev/null || {
    rm -rf ./.git/hooks
    cp -r ./dev/hooks ./.git/hooks
  }
}

log 're/configuring hooks...'
install_hooks
