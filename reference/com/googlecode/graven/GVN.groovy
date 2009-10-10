package com.googlecode.graven;

import groovy.xml.MarkupBuilder
import javax.xml.parsers.DocumentBuilderFactory
import groovy.xml.dom.DOMCategory


private def createPom(dir = ".") {
    // Read the pom.groovy script
    GroovyShell shell = new GroovyShell()
    Script script = shell.parse(new File(dir, "pom.groovy"))

    // Write out a pom.xml
    File pomFile = new File(dir, "pom.xml")
    def fw = new FileWriter(pomFile);

    // Setup the markup builder
    MarkupBuilder mb = new MarkupBuilder(fw)

    // Bind the markup builder into the script
    def binding = new Binding()
    binding.setProperty "project", {pom ->
        mb.project {
            pom.owner = script
            pom.delegate = mb
            pom()
        }
    }

    // Include all of the variables on the included script in our binding
    def includeClass = {clazz ->
        def include = clazz.newInstance()
        include.run()
        include.binding.properties.variables.each {
            binding.setVariable it.key, it.value.curry(mb)
        }
    }

    def includeFile = {file ->
        def include = shell.parse(new File(dir, file))
        include.run()
        include.binding.properties.variables.each {
            binding.setVariable it.key, it.value.curry(mb)
        }
    }

    // Standard library
    includeClass(Pom)

    // Add the includer to the binding
    binding.setProperty "include", includeClass
    binding.setProperty "includeFile", includeFile

    // Set the script binding and execute
    script.binding = binding
    def result = script.run();
    fw.close();

    def builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    def inputStream = new FileInputStream(pomFile)
    def document = builder.parse(inputStream)

    use(DOMCategory.class) {
        document.getDocumentElement().modules.module.each {
            createPom(it.text());
        }
    }
}

createPom();

// Excecute maven 2
def list = ["mvn"]
list.addAll(args as List)
new ProcessBuilder(list).redirectErrorStream(true).start().in.eachLine {
    println it
}