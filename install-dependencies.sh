#!/bin/bash

# Fix the CircleCI path
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$PATH"

DEPS="$ANDROID_HOME/installed-dependencies"

if [ ! -e $DEPS ]; then
  cp -r /usr/local/android-sdk-linux $ANDROID_HOME &&
  echo y | android update sdk -u -a -t android-20 &&
  echo y | android update sdk -u -a -t platform-tools &&
  echo y | android update sdk -u -a -t build-tools-20.0.0 &&
  echo y | android update sdk -u -a -t sys-img-x86-android-20 &&
  echo y | android update sdk -u -a -t addon-google_apis-google-20 &&
  echo n | android create avd -n testing -f -t android-20 &&
  touch $DEPS
fi
