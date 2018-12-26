#!/usr/bin/env bash

source bin/includes.sh

safeEval "./gradlew clean test check"
