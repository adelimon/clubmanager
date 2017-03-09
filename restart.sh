#!/bin/bash
rm nohup.out
pkill java
nohup ./mvnw &
tail -f nohup.out
