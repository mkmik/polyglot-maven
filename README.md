Description
-----------

Sonatype [Polyglot Maven](https://docs.sonatype.org/display/PMAVEN/Home).

License
-------

[Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

Building
--------

### Requirements

* [Maven](http://maven.apache.org) 2+
* [Java](http://java.sun.com/) 5+

Check-out and build:

    git clone git@github.com:sonatype/polyglot-maven.git
    cd polyglot-maven
    mvn install

After this completes, you can unzip the assembly play with polyglot maven:

    unzip pmaven-cli/target/pmaven-*-bin.zip
    ./pmaven-cli*/bin/mvn

Porting
-------

### Using the 'translate' command

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
