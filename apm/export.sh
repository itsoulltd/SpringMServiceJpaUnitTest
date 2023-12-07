#!/usr/bin/env bash
echo "Exporting Glowroot jar"
#unzip glowroot-0.13.6-dist.zip -d /Users/$USER/Downloads
export AGENT_GLOWROOT=/Users/$USER/Downloads/glowroot
source ~/.bash_profile
echo "$AGENT_GLOWROOT added"
##Finally Call you jar as Follow
## java -javaagent:$AGENT_GLOWROOT/glowroot.jar -jar MyApp.jar
##OR pass in VM Options:
## -javaagent:$AGENT_GLOWROOT/glowroot.jar
## -javaagent:/Users/$USER/Downloads/glowroot/glowroot.jar