#!/usr/bin/env bash

sbt clean scalafmtAll compile coverage Test/test it/Test/test coverageOff dependencyUpdates coverageReport
