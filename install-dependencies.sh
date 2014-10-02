#!/bin/bash

# Fix the CircleCI path
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$PATH"

DEPS="$ANDROID_HOME/installed-dependencies"

if [ ! -e $DEPS ]; then
  cp -r /usr/local/android-sdk-linux $ANDROID_HOME &&
  echo y | android update sdk -u -a -t android-L > /dev/null &&
  echo y | android update sdk -u -a -t android-19 > /dev/null &&
  echo y | android update sdk -u -a -t platform-tools > /dev/null &&
  echo y | android update sdk -u -a -t build-tools-20.0.0 > /dev/null &&
  echo y | android update sdk -u -a -t sys-img-x86-android-19 > /dev/null &&
  echo y | android update sdk -u -a -t addon-google_apis-google-18 > /dev/null &&
  echo y | android update sdk -u -a -t extra-google-m2repository > /dev/null &&
  echo y | android update sdk -u -a -t extra-android-m2repository > /dev/null &&
  echo n | android create avd -n testing -f -t android-19 &&
  touch $DEPS
fi
