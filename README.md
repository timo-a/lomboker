# Lomboker

## Purpose

This command supports migrating a large code base to lombok. 
Eclipse/IntelliJ can migrate one class in a few clicks but that may not be fast enough for you.
Lomboker helps you scale.

## Usage

### Running from source
There are java files in the test resources to try it out
```
lomboker$ cp ./lib/src/test/resources/ClassAInput.java ./lib/src/test/resources/ClassAOutput.java
lomboker$ ./gradlew :app:run --args=".reduce getter ./lib/src/test/resources/ClassAInput.java"
```
### Building from source
```
lomboker$ ./gradlew :app:assemble
```

### Converting a project
Lomboker does not aim to automatically convert a whole project but is meant to be used with other command line utils.

```
alias lomboker='java -jar ./app/build/libs/lomboker.jar'
find . -name '*.java' > allJavaFiles.txt
```

#### Analysis

`count` takes a list (in the form of a file) of java files and outputs for each:  
`filename #trivialGetters #fuzzyGetters #triviaSetters #fuzzySetters`  
You can sum those numbers up and display them in a table.
```
lomboker count allJavaFiles.txt > counts.txt
cat counts.txt | awk '{gt+=$2; gf+=$3; st+=$4; sf+=$5} END {printf "     trivial fuzzy\ngetter %5d %5d\nsetter %5d %5d\n", gt,gf,st, sf}'
```

### Reduce Getters

```
cat counts.txt | awk '{print $1, $2}' | grep " 0$" | awk '{print $1}' > getterClasses.txt
while read f; do lomboker reduce getter "$f"; done < getterClasses.txt;
# put annotations on their own lines // capture first annotation (1) and  white space before it (2)
cat counts.txt | xargs sed 's/^\(\(\s\s*\)@\w\w*(\([0-9a-zA-Z", ])\)?\) @Getter/\1\n\2@Getter/'
```

I did not find a way to make the JavaParser API put additional annotations on their own line, so we need to run a sed after it inserting newlines where necessary.

### Reduce Setters
```
cat counts.txt | awk '{print $1, $4}' | grep " 0$" | awk '{print $1}' > setterClasses.txt
while read f; do lomboker reduce setter "$f"; done < setterClasses.txt;
# put annotations on their own lines // capture first annotation (1) and  white space before it (2)
cat counts.txt | xargs sed 's/^\(\(\s\s*\)@\w\w*(\([0-9a-zA-Z", ])\)?\) @Setter/\1\n\2@Setter/'
```
### Mark fuzzy getters

Some methods behave like getters but have a different name than the one lombok creates of the field in question.
In these cases lomboker marks such a method by inserting a line "the mark" into the code.
You have to manually go through all occurrences of the mark, decide on a name, rename field or method and update references to it. 
After that you can apply reduce getter on these files.
```
cat counts.txt | awk '{print $1, $3}' | grep " 0$" | awk '{print $1}' > fuzzyGetters.txt
while read f; do lomboker mark getter "$f"; done < fuzzyGetters.txt;
while read f; do sed -i "s|TODO Lomboker says check|//TODO Lomboker says check|g" "$f"; done < fuzzyGetters.txt;
```

My goal for the mark is to be an extra line that can easily be deleted.
I did not find a way to make the JavaParser API put comments over a method or at the start of the method body without interfering with existing comments.
So instead I used a string expression `"TODO Lomboker says check this potential getter".trim();` with goes at the end of the method body.
(I couldn't put it in front because then the indentation vanished.)
But with this expression at the end the code no longer compiles because unreachable code is that severe apparently.
The easiest fix for this is to turn those expressions into a comment in a second pass with sed.
I welcome any PRs fixing this.

### Mark fuzzy setters
```
cat counts.txt | awk '{print $1, $5}' | grep " 0$" | awk '{print $1}' > fuzzySetters.txt
while read f; do lomboker mark setter "$f"; done < fuzzySetters.txt;
while read f; do sed -i "s|TODO Lomboker says check|//TODO Lomboker says check|g" "$f"; done < fuzzySetters.txt;
```
### Summarize getter, setter
Replaces all method level annotations with one class level annotation.

### Reduce Equals and hash code
This reduces `equals` and `hashCode` if both appear. Use git tools to review the changes.

### Reduce toString
### Reduce no args constructor