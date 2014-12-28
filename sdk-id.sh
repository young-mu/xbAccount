#!/bin/bash

sdkId=`cat ./sdk-id`
sed -i "s/xxxxx/${sdkId}/g" ./src/com/young/account/ViewActivity.java
