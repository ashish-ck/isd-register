#!/bin/#!/bin/sh

( cd opsmx-isd-register ; mvn install dockerfile:push )
[ $? == 1 ] && {
  echo "Building docker images failed"
  exit 1
}

exit 0
