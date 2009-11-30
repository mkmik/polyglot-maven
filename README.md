Description
-----------

Sonatype Polyglot Maven.

Building
--------

### Requirements

* [Maven](http://maven.apache.org) 2.x
* [Java](http://java.sun.com/) 5

Check-out and build:

    git clone git@github.com:sonatype/graven.git pmaven
    cd pmaven
    mvn install

After this completes, you can unzip the assembly and have a go with the shell:

    unzip pmaven-cli/target/pmaven-*-bin.zip
    ./pmaven-cli*/bin/mvn
