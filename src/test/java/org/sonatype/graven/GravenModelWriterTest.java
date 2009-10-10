package org.sonatype.graven;

import junit.framework.TestCase;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.DefaultModelWriter;
import org.codehaus.plexus.util.IOUtil;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

/**
 * Tests for {@link org.sonatype.graven.GravenModelWriter}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class GravenModelWriterTest
    extends TestCase
{
    private GravenModelWriter writer;

    @Override
    public void setUp() throws Exception {
        writer = new GravenModelWriter();
    }

    private String load(final String name) throws IOException {
        assertNotNull(name);
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
        Model model = new Model();
        Parent parent = new Parent();
        parent.setGroupId("a");
        parent.setArtifactId("c");
        parent.setVersion("c");
        model.setParent(parent);

        StringWriter buff = new StringWriter();
        writer.write(buff, null, model);

        System.out.println(buff);

        String expected = load("test2.groovy");
        assertEquals(expected, buff.toString());
    }
}