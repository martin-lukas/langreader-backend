#!/bin/bash

sudo kill $(sudo lsof -i:443 -t)

# Run the server in the background
nohup sudo java -jar langreader.jar > server.out 2> server.err &