#!/bin/#!/bin/sh

( cd opsmx-isd-register ; mvn clean install dockerfile:build )
[ $? == 1 ] && {
  echo "Building docker images failed"
  exit 1
}

exit 0
