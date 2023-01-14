#!/bin/bash
export PATH="$PATH:"

sed -i "" "s/@RemoteImplementation/@RemoteInterface/g" src/main/java/de/haw/vsp/tron/view/screens/IScreenHandler.java
sed -i "" "s/@RemoteInterface/@RemoteImplementation/g" src/main/java/de/haw/vsp/tron/controller/scenechanger/ISceneChanger.java
sed -i "" "s/@RemoteInterface/@RemoteImplementation/g" src/main/java/de/haw/vsp/tron/controller/playercontrol/IPlayerInputManager.java

mvn -f pom-model-controller.xml clean package
mv target/model-controller-0.1-jar-with-dependencies.jar ./
