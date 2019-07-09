#!/bin/bash

# Here you can write the commands to install your dependencies
#apt install -y vim-full

# Here you can write the command used to execute your software
#java -jar my-fat.jar --input1=$INPUT_FOLDER/input1/ --input2=$INPUT_FOLDER/input2/ --output=$OUTPUT_FOLDER/data/ --logs=$OUTPUT_FOLDER/logs/
java -jar wordCount.jar "$@"