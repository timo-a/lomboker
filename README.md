# Lomboker

## Purpose

This command supports migrating a large code base to lombok. 
Eclipse/IntelliJ can migrate one class in a few clicks but that may not be fast enough for you.
Lomboker helps you scale.

## Usage

```
lomboker$ cp ./lib/src/test/resources/ClassAInput.java ./lib/src/test/resources/ClassAOutput.java
lomboker$ ./gradlew :app:run --args=".reduce getter ./lib/src/test/resources/ClassAInput.java"

lomboker$ #./gradlew :app-getter:assemble
lomboker$ #java -jar ./app-getter/build/libs/lomboker.jar reduce getter lib/src/test/resources/ClassAInput.java
```


## TODO
- github actions
   - test required for push
   - build jars
