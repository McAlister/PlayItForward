#! /bin/bash

rm -f dist/*
grails clean
grails prod war
mkdir -p dist/
cp build/libs/PlayItForward-0.1.war dist/
