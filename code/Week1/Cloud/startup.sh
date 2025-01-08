#!/bin/sh
sudo apt update
sudo apt -y install build-essential
sudo apt update
sudo apt -y install git gcc gpp vim htop tree
   

# install JAVA environment
sudo apt -y install  default-jre
java --version
sudo apt -y install default-jdk
javac --version
sudo apt -y install maven