#!/bin/bash


cat src/main/resources/SpaceGroupTable.txt | grep -A 1 '<a name="' |
	sed 's:<a name=\":HALLO:'
