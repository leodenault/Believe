#!/bin/bash
#
# Based on the contents available at https://rjzaworski.com/2018/01/keeping-git-hooks-in-sync

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
BIN_LOCATION=$DIR/bin

cd "$DIR/.."

log() {
  echo "$(basename ${BASH_SOURCE[0]}): $@"
}

install_hooks() {
  if [ -L ./.git/hooks ]; then
    return 1
  fi

  rm -rf ./.git/hooks
  ln -s ../dev/hooks ./.git/hooks
}

install_dev_tools() {
  # Check that a tools directory exists in the project.
  if [ -d $BIN_LOCATION ]; then
    return 1
  fi

  mkdir $BIN_LOCATION

  # Install ktlint.
  log 'Installing ktlint.'
  curl -sSLO https://github.com/pinterest/ktlint/releases/download/0.42.1/ktlint &&
    chmod a+x ktlint &&
    sudo mv ktlint $BIN_LOCATION

  # Install Google Java Format.
  log 'Installing Google Java Format.'
  GJF_VERSION=1.11.0
  curl -sSLO https://github.com/google/google-java-format/releases/download/v$GJF_VERSION/google-java-format-$GJF_VERSION-all-deps.jar &&
    sudo chmod a+x google-java-format-$GJF_VERSION-all-deps.jar &&
    mv google-java-format-$GJF_VERSION-all-deps.jar $BIN_LOCATION

  # Download the Python diff script.
  GJF_SOURCE_ZIP_NAME="v$GJF_VERSION.zip"
  SCRIPT_NAME="google-java-format-diff.py"
  TOP_LEVEL_DIR_IN_ZIP="google-java-format-$GJF_VERSION"
  ZIP_SCRIPT_LOCATION="$TOP_LEVEL_DIR_IN_ZIP/scripts/$SCRIPT_NAME"
  curl -sSLO https://github.com/google/google-java-format/archive/refs/tags/$GJF_SOURCE_ZIP_NAME &&
    unzip -q $GJF_SOURCE_ZIP_NAME $ZIP_SCRIPT_LOCATION &&
    mv $ZIP_SCRIPT_LOCATION $BIN_LOCATION/$SCRIPT_NAME &&
    sudo chmod a+x $BIN_LOCATION/$SCRIPT_NAME
  rm $GJF_SOURCE_ZIP_NAME
  rm -rf $TOP_LEVEL_DIR_IN_ZIP
}

log 'Checking if Git hooks are configured...'
if install_hooks; then
  log 'Git hooks are configured!'
else
  log 'Git hooks already configured.'
fi

log 'Checking if dev tools are installed...'
if install_dev_tools; then
  log 'Dev tools are installed!'
else
  log 'Dev tools already installed.'
fi
