#!/bin/bash

mvn -Dmaven.test.skip=true package
java -jar target/SpaceGroup_Visualizer-1.0-SNAPSHOT.jar
