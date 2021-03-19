# Lomboker

## Purpose

This command supports migrating a large code base to lombok. 
Eclipse/IntelliJ can migrate one class in a few clicks but that may not be fast enough for you.
Lomboker helps you scale.

## Usage

Running from source
```
lomboker$ cp ./lib/src/test/resources/ClassAInput.java ./lib/src/test/resources/ClassAOutput.java
lomboker$ ./gradlew :app:run --args=".reduce getter ./lib/src/test/resources/ClassAInput.java"
```
Building from source
```
lomboker$ ./gradlew :app:assemble
```
Converting a project
```
alias lomboker='java -jar ./app/build/libs/lomboker.jar'
#analysis
find . -name '*.java' | lomboker count > counts.txt
cat counts.txt | awk '{gt+=$2; gf+=$3; st+=$4; sf+=$5} END {printf "     trivial fuzzy\ngetter %5d %5d\nsetter %5d %5d\n", gt,gf,st, sf}'
cat counts.txt | awk '{print $1 $2}' | grep " 0$" | awk '{print $1}' > getterClasses.txt
# reduce getters
while read f; do lomboker reduce getter "$f"; done < getterClasses.txt;
# put annotations on their own lines // capture first annotation (1) and  white space before it (2)
cat counts.txt | xargs sed 's/^\(\(\s\{1,\}\)@\w\{1,\}\) @Getter/\1\n\2@Getter/' /tmp/test.java
```


## TODO
- github actions
   - test required for push
   - build jars
   - delete javadoc
   - stop when there is annotation
   - getter setter shall have their own line 