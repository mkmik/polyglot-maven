package org.sonatype.graven

import org.apache.maven.model.Model
import org.apache.maven.model.Parent
import org.apache.maven.model.io.DefaultModelWriter
import org.junit.Before
import org.junit.Test
import org.sonatype.graven.GravenModelWriter
import static org.junit.Assert.*

/**
 * Tests for {@link org.sonatype.graven.GravenModelWriter}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class GravenModelWriterTest
    extends GravenModelTestSupport
{
    private GravenModelWriter writer

    @Before
    public void setUp() throws Exception {
        writer = new GravenModelWriter()
    }

    @Test
    public void test1() throws Exception {
        def model = new Model()
        def parent = new Parent()
        parent.groupId = "a"
        parent.artifactId = "b"
        parent.version = "c"
        model.parent = parent

        def buff = new StringWriter()
        writer.write(buff, null, model)

        println(buff)

        def expected = load("test2.groovy")
        assertEquals(expected, buff.toString())
    }
}