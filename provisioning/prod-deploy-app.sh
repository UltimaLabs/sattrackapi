#!/bin/bash

# RUN THE BUILD

cd ..
./gradlew clean buildDeployJar

if [ $? -ne 0 ]; then
    echo "Build failed."
    exit 1
fi

# PATH TO THE JAR FILE
export JAR_FILE=$(basename $(ls -1 provisioning/roles/deploy-app/files/*.jar))

# RUN THE ANSIBLE PLAYBOOK

cd provisioning
./playbook-prod-deploy-app.yml --extra-vars "app_jar_file=$JAR_FILE"
