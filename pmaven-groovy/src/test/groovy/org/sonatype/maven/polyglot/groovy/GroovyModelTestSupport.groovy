package org.sonatype.maven.polyglot.groovy

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import org.apache.maven.model.Model
import org.apache.maven.model.io.DefaultModelWriter
import org.sonatype.maven.polyglot.groovy.Dom2Groovy
import org.w3c.dom.Document
import org.xml.sax.InputSource
import static org.junit.Assert.*
import org.sonatype.maven.polyglot.groovy.Dom2Groovy
import org.codehaus.plexus.PlexusTestCase

/**
 * Support for model tests.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class GroovyModelTestSupport
    extends PlexusTestCase
{
    protected String load(final String name) {
        assertNotNull(name)
        def url = getClass().getResource(name)
        assertNotNull(url)
        return url.text
    }

    protected String chew(final Model model) {
        assertNotNull(model)
        def writer = new DefaultModelWriter()
        def buff = new StringWriter()
        writer.write(buff, null, model)
        return buff.toString()
    }

    protected void dump(final Model model) {
        def buff = new StringWriter()
        Dom2Groovy converter = new Dom2Groovy(new IndentPrinter(new PrintWriter(buff), "    "))
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        Document doc = builder.parse(new InputSource(new StringReader(chew(model))))
        
        def root = doc.documentElement
        def attrs = root.attributes
        for (int i=0; i<attrs.length; i++) {
            root.removeAttribute(attrs.item(i).name)
        }
        // Not sure where this comes from but the above will not nuke it
        root.removeAttribute("xmlns:xsi")
        
        converter.print(doc)
        println(buff)
    }
}
