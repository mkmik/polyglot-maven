package org.sonatype.maven.graven;

import groovy.util.IndentPrinter;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.DefaultModelWriter;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.polyglot.io.ModelWriterSupport;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Writes a Maven {@link org.apache.maven.model.Model} to a <tt>pom.groovy</tt>.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role=ModelWriter.class, hint="graven")
public class GravenModelWriter
    extends ModelWriterSupport
{
    public void write(final Writer output, final Map<String,Object> options, final Model model) throws IOException {
        assert output != null;
        assert model != null;

        StringWriter buff = new StringWriter();
        DefaultModelWriter writer = new DefaultModelWriter();
        writer.write(buff, options, model);

        Dom2Groovy converter = new Dom2Groovy(new IndentPrinter(new PrintWriter(output), "    "));

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(buff.toString())));

            Element root = doc.getDocumentElement();
            NamedNodeMap attrs = root.getAttributes();
            for (int i=0; i<attrs.getLength(); i++) {
                Attr attr = (Attr)attrs.item(i);
                root.removeAttribute(attr.getName());
            }
            // Not sure where this comes from but the above will not nuke it
            root.removeAttribute("xmlns:xsi");
            
            converter.print(doc);
            output.flush();
        }
        catch (ParserConfigurationException e) {
            throw new IOException(e);
        }
        catch (SAXException e) {
            throw new IOException(e);
        }
    }
}