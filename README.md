Description
-----------

Sonatype Polyglot Maven.

Building
--------

### Requirements

* [Maven](http://maven.apache.org) 2.x
* [Java](http://java.sun.com/) 5

Check-out and build:

    git clone git@github.com:sonatype/polyglot-maven.git
    cd polyglot-maven
    mvn install

After this completes, you can unzip the assembly play with polyglot maven:

    unzip pmaven-cli/target/pmaven-*-bin.zip
    ./pmaven-cli*/bin/mvn

Porting Languages
-----------------

### Components

Plexus components need to be implemented for:

 * org.sonatype.maven.polyglot.mapping.Mapping (extend org.sonatype.maven.polyglot.mapping.MappingSupport)
 * org.apache.maven.model.io.ModelReader (extend org.sonatype.maven.polyglot.io.ModelReaderSupport)
 * org.apache.maven.model.io.ModelWriter (extend org.sonatype.maven.polyglot.io.ModelWriterSupport)

All of these components need to be proper Plexus components wither using the annotations (preferred IMO) or suppling a components.xml.

#### Execute Support

You can ignore the *.execute stuff for now, its highly experimental.

### Hooking up to the CLI

To hook up to the cli (so the mvn and translate commands see new languages) the following files need to be updated:

##### pmaven-cli/pom.xml

Add a dep on the new language module

##### pmaven-cli/src/main/assembly/common.xml

Define a new dependencySet to install the required jars into the ext dir.

### Using the 'translate' Command

Use the translate command, to help make sure that everything is working correctly.  Then once you think it is,
then update the PolyglotTranslatorCliTest (in pmaven-cli/src/test/...) to enable the language in testFormatInterchange().

For using translate, use the top-level pom in polyglot-maven, so after building,
unzip the assembly and then to test groovy:

    ./pmaven-cli*/bin/translate pom.xml pom.groovy
    ./pmaven-cli*/bin/translate pom.groovy pom-groovy.xml

pom.xml and pom-groovy.xml should be structurally equivalent.

For a quick check using diff you can do this:

    ./pmaven-cli*/bin/translate pom.xml pom-norm.xml

Then a simple:

    diff pom-norm.xml pom-groovy.xml

If this works, then the build should work too, but be sure to give it a whirl too with:

    ./pmaven-cli*/bin/mvn -f pom.groovy

The .groovy ext here is just an example, and it should work.
But obviously you'll want to use the ext configured in the language mapping.
