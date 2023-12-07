#!/usr/bin/env bash
echo "Exporting Glowroot jar"
if [ -d glowroot ]; then
  echo "Directory exists"
else
  echo "Unzip glowroot-xxx.zip"
  unzip glowroot-0.13.6-dist.zip
fi

echo 'Paste the following line as VM option to the mvn cmd'
echo -javaagent:$PWD/glowroot/glowroot.jar

###Optional:
#export AGENT_GLOWROOT=/Users/$USER/Downloads/glowroot
##source ~/.bash_profile OR .zshrc
#if [ -d AGENT_GLOWROOT ]; then
#  echo "Directory exists"
#else
#  echo "Unzip glowroot-xxx.zip"
#  unzip glowroot-0.13.6-dist.zip -d /Users/$USER/Downloads
#fi
#echo 'Paste the following line as VM option to the mvn cmd'
#echo -javaagent:$AGENT_GLOWROOT/glowroot.jar

##Finally Call you jar as Follow
## java -javaagent:$AGENT_GLOWROOT/glowroot.jar -jar MyApp.jar
##OR pass in VM Options:
## -javaagent:$AGENT_GLOWROOT/glowroot.jar

##Now access glowroot ui: http://localhost:4000