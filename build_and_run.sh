#!/bin/bash

mvn package
java -jar target/SpaceGroup_Visualizer-1.0-SNAPSHOT.jar
