#!/bin/bash

# Target port to wait for
TARGET_PORT=8080
# Timeout in seconds
TIMEOUT=60
# Start time
start_time=$(date +%s)

while true; do
  # Current time
  current_time=$(date +%s)
  # Elapsed time
  elapsed_time=$((current_time - start_time))

  # Check for timeout
  if [ $elapsed_time -ge $TIMEOUT ]; then
    echo "Error: Timeout while waiting for port $TARGET_PORT to become available."
    exit 1
  fi

  # Check if the port is open
  netstat -an | grep $TARGET_PORT > /dev/null
  if [ $? -eq 0 ]; then
    echo "Port $TARGET_PORT is now available."
    exit 0
  fi

  # Wait a bit before retrying
  sleep 1
done

