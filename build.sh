#!/bin/bash
git pull
git status
echo "Erasing all changes currently present in this workspace with the latest from github..."
git checkout ./
#nohup mvnw
