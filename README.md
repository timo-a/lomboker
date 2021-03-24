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
# reduce getters
cat counts.txt | awk '{print $1, $2}' | grep " 0$" | awk '{print $1}' > getterClasses.txt
while read f; do lomboker reduce getter "$f"; done < getterClasses.txt;
# put annotations on their own lines // capture first annotation (1) and  white space before it (2)
cat counts.txt | xargs sed 's/^\(\(\s\s*\)@\w\w*(\([0-9a-zA-Z", ])\)?\) @Getter/\1\n\2@Getter/'
# reduce setters
cat counts.txt | awk '{print $1, $4}' | grep " 0$" | awk '{print $1}' > setterClasses.txt
while read f; do lomboker reduce setter "$f"; done < setterClasses.txt;
# put annotations on their own lines // capture first annotation (1) and  white space before it (2)
cat counts.txt | xargs sed 's/^\(\(\s\s*\)@\w\w*(\([0-9a-zA-Z", ])\)?\) @Setter/\1\n\2@Setter/'
# mark fuzzy getters
cat counts.txt | awk '{print $1, $3}' | grep " 0$" | awk '{print $1}' > fuzzyGetters.txt
while read f; do lomboker mark getter "$f"; done < fuzzyGetters.txt;
# mark fuzzy setters
cat counts.txt | awk '{print $1, $5}' | grep " 0$" | awk '{print $1}' > fuzzySetters.txt
while read f; do lomboker mark setter "$f"; done < fuzzySetters.txt;
```


## TODO
- github actions
   - test required for push
   - build jars
   - getter setter shall have their own line 
