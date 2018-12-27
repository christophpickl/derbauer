#!/usr/bin/env bash

./gradlew detekt
open build/reports/detekt/detekt.html
