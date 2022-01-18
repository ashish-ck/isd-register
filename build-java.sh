#!/bin/#!/bin/sh

( cd opsmx-isd-register ; mvn -e clean install )
[ $? == 1 ] && {
  echo "Building opsmx-isd-register failed"
  exit 1
}

exit 0

