#!/usr/bin/env bash
USER_ID=$(adb shell am get-current-user | tr -d '\r')
echo "Current user: $USER_ID"
echo "Current HOME:"adb shell cmd package resolve-activity \    --brief \    --user "$USER_ID" \    -a android.intent.action.MAIN \    -c android.intent.category.HOME
echo "Available HOME activities:"adb shell cmd package query-activities \    --brief \    --user "$USER_ID" \    -a android.intent.action.MAIN \    -c android.intent.category.HOME
echo "Setting custom launcher..."adb shell cmd package set-home-activity \    --user "$USER_ID" \    com.aosplab.composecarhome
adb shell input keyevent KEYCODE_HOME
echo "New HOME:"adb shell cmd package resolve-activity \    --brief \    --user "$USER_ID" \    -a android.intent.action.MAIN \    -c android.intent.category.HOME
