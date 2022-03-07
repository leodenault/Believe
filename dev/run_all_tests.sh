#!/bin/bash

TOP_LEVEL_DIR=$(git rev-parse --show-toplevel)

bash -ic "$TOP_LEVEL_DIR/bazelisk test //javatests/...:all"
