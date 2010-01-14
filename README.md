Description
-----------

Sonatype [Polyglot Maven](http://polyglot.sonatype.org/).

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
