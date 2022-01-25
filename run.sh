#!/bin/#!/bin/sh

( cd opsmx-isd-register ; mvn spring-boot:run )
[ $? == 1 ] && {
  echo "Running opsmx-isd-register failed"
  exit 1
}

exit 0

