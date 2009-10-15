package org.sonatype.maven.graven

import org.apache.maven.model.Model
import org.apache.maven.model.Parent
import org.junit.Before
import org.junit.Test
import org.sonatype.maven.graven.GravenModelTestSupport
import org.sonatype.maven.graven.GravenModelWriter
import static org.junit.Assert.*
import org.sonatype.maven.graven.GravenModelWriter

/**
 * Tests for {@link org.sonatype.maven.graven.GravenModelWriter}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class GravenModelWriterTest
    extends GravenModelTestSupport
{
    private GravenModelWriter writer

    @Before
    void setUp() {
        writer = new GravenModelWriter()
    }

    @Test
    void testWriting() {
        def model = new Model(parent: new Parent(groupId: "a", artifactId: "b", version: "c"))

        def buff = new StringWriter()
        writer.write(buff, null, model)

        println(buff)

        def expected = load("test2.groovy")
        assertEqualsGroovy(expected, buff.toString())
    }

    private void assertEqualsGroovy( String expected, String actual ) {
        def text = actual.replaceAll( "(\r\n)|(\r)|(\n)", "\n" )
        def expect = expected.replaceAll( "(\r\n)|(\r)|(\n)", "\n" )
        assertEquals(expect, text)
    }

}