# Lomboker

## Purpose

This command supports migrating a large code base to lombok. 
Eclipse/IntelliJ can migrate one class in a few clicks but that may not be fast enough for you.
Lomboker helps you scale.

## Usage

```
lomboker$ cp ./lib/src/test/resources/ClassAInput.java ./lib/src/test/resources/ClassAOutput.java
lomboker$ ./gradlew :app:run --args=".reduce getter ./lib/src/test/resources/ClassAInput.java"

lomboker$ ./gradlew :app-getter:assemble
lomboker$ alias lomboker='java -jar ./app-getter/build/libs/lomboker.jar'
lomboker$ lomboker reduce getter lib/src/test/resources/ClassAInput.java
lomboker$ ls lib/src/test/resources/Class*.java | lomboker count | column -t
lomboker$ ls lib/src/test/resources/Class*.java | lomboker count | awk '{gt+=$2; gf+=$3; st+=$4; sf+=$5} END {printf "     trivial fuzzy\ngetter %5d %5d\nsetter %5d %5d\n", gt,gf,st, sf}'
```


## TODO
- github actions
   - test required for push
   - build jars
