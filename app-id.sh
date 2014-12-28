#!/bin/bash

appID=`cat ./app-id`
sed -i "s/xxxxx/${appID}/g" ./src/com/young/account/ViewActivity.java
