#!/usr/bin/env bash
cd ~/Work/workspace/volley/
echo Go to volley master
git pull
echo Pull successfully
cp -R ~/Work/workspace/volley/src/main/java/com/android/volley/*  ~/Work/studio/Common/googlevolleynetwork_lib/src/main/java/com/android/volley
echo Copy successfully
sleep 2