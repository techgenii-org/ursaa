#!/bin/bash

# AWS EC2 instance details
EC2_INSTANCE_IP="3.109.139.95"
EC2_INSTANCE_USERNAME="ubuntu"
EC2_INSTANCE_KEY_PATH="/Users/saifullahkhan/projects/ursaa/ursaa.pem"

# Local Maven project details
MAVEN_PROJECT_PATH="/Users/saifullahkhan/projects/ursaa"
MAVEN_BUILD_CMD="mvn clean package -DskipTests"

# Local JAR file details
LOCAL_JAR_PATH="$MAVEN_PROJECT_PATH/target/ursaa-1.0.0.jar"

echo $LOCAL_JAR_PATH

# Remote destination on EC2 instance
REMOTE_DESTINATION="/home/ubuntu/app/"

# SSH connection string
SSH_CONNECTION="$EC2_INSTANCE_USERNAME@$EC2_INSTANCE_IP"

# Step 1: Create Maven build
echo "Step 1: Creating Maven build"
cd $MAVEN_PROJECT_PATH || exit
echo $MAVEN_BUILD_CMD
$MAVEN_BUILD_CMD || exit

# Step 2: Upload the build to AWS EC2 instance
echo "Step 2: Uploading the build to AWS EC2 instance"
scp -i $EC2_INSTANCE_KEY_PATH $LOCAL_JAR_PATH $SSH_CONNECTION:$REMOTE_DESTINATION || exit

# Step 3: Stop any running Java application on EC2 instance
echo "Step 3: Stopping any running Java application on EC2 instance"
ssh -i $EC2_INSTANCE_KEY_PATH $SSH_CONNECTION "pkill -f ursaa-1.0.0.jar"

# Step 4: Run the uploaded JAR file on EC2 instance
echo "Step 4: Running the uploaded JAR file on EC2 instance"
ssh -i $EC2_INSTANCE_KEY_PATH $SSH_CONNECTION "nohup java -jar -Dspring.profiles.active=server $REMOTE_DESTINATION/ursaa-1.0.0.jar > /dev/null 2>&1 &" || exit

echo "Deployment and execution completed successfully."



