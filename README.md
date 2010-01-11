Description
-----------

Sonatype [Polyglot Maven](https://docs.sonatype.org/display/PMAVEN/Home).

License
-------

[Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

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
