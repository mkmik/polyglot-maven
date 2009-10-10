package org.sonatype.graven;

import junit.framework.TestCase;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.DefaultModelWriter;
import org.codehaus.plexus.util.IOUtil;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

/**
 * Tests for {@link GravenModelReader}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class GravenModelReaderTest
    extends TestCase
{
    private GravenModelReader reader;

    @Override
    public void setUp() throws Exception {
        reader = new GravenModelReader();
    }

    private String load(final String name) throws IOException {
        URL url = getClass().getResource(name);
        assertNotNull(url);
        return IOUtil.toString(url.openStream());
    }

    private String chew(final Model model) throws IOException {
        assertNotNull(model);
        DefaultModelWriter writer = new DefaultModelWriter();
        StringWriter buff = new StringWriter();
        writer.write(buff, null, model);
        return buff.toString();
    }

    public void test1() throws Exception {
        URL input = getClass().getResource("test1.groovy");
        assertNotNull(input);

        Model model = reader.read(input.openStream(), null);

        String xml = chew(model);

        System.out.println(xml);

        String expectedXml = load("test1.xml");
        assertEquals(expectedXml, xml);
    }
}
