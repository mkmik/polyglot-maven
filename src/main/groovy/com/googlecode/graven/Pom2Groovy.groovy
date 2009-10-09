package com.googlecode.graven;

import javax.xml.parsers.DocumentBuilderFactory
import org.codehaus.groovy.tools.xml.DomToGroovy

def builder     = DocumentBuilderFactory.newInstance().newDocumentBuilder()
def inputStream = new FileInputStream(args[0])
def document    = builder.parse(inputStream)
def output      = new StringWriter()
def converter   = new DomToGroovy(new PrintWriter(output))

converter.print(document)
new File(args[1]).asWritable().write output.toString()
