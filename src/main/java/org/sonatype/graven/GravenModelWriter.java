package org.sonatype.graven;

import groovy.util.IndentPrinter;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.DefaultModelWriter;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.groovy.tools.xml.DomToGroovy;
import org.codehaus.plexus.util.IOUtil;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
public class GravenModelWriter
    implements ModelWriter
{
    public void write(final File file, final Map<String,Object> options, final Model model) throws IOException {
        assert file != null;
        assert model != null;

        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        try {
            write(out, options, model);
            out.flush();
        }
        finally {
            IOUtil.close(out);
        }
    }

    public void write(final OutputStream output, final Map<String,Object> options, final Model model) throws IOException {
        assert output != null;
        assert model != null;

        write(new OutputStreamWriter(output), options, model);
    }

    public void write(final Writer output, final Map<String,Object> options, final Model model) throws IOException {
        assert output != null;
        assert model != null;

        StringWriter buff = new StringWriter();
        DefaultModelWriter writer = new DefaultModelWriter();
        writer.write(buff, options, model);

        DomToGroovy converter = new DomToGroovy(new IndentPrinter(new PrintWriter(output), "    "));

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(buff.toString())));
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