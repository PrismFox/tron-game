# view-library
Tron View JavaFX Library used BAI5-VSP lab for TRON game.
Javadoc is also generated by maven.

## Prerequisites
- Java >= 8
- Maven

## Build and Install

```bash
# Might need to set java version
export JAVA_HOME=/usr/java/java11/

# builds and installs to local maven repository at ~/.m2
mvn install

# if tests fail: a (dirty) workaround
mvn -DskipTests install

```

## Requirements for playing the game
- configMiddleware.properties contains the right ip of the nameserver
- The execution of the commands takes place in the given order

```bash
# Creates the jars for the view, model/controller and nameserver
./script_packages_creation

# It's required that someone starts the nameserver
java -jar nameserver-0.1-jar-with-dependencies.jar

# One person must start the model/controller. It could be even the person who started the nameserver 
java -jar model-controller-0.1-jar-with-dependencies.jar

# Every host needs to start the view in order to participate 
java -jar view-library-0.1-jar-with-dependencies.jar
```

 