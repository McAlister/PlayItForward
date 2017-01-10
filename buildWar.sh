#! /bin/bash

rm dist/*
grails clean
grails prod war
cp build/libs/PlayItForward-0.1.war dist/
