#!/bin/bash
echo starting sqlmapapi
nohup sqlmapapi -s &
echo starting msfrpc
nohup msfrpcd -U msf -P msf -S -f &