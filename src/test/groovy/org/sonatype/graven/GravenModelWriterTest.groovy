package org.sonatype.graven

import org.apache.maven.model.Model
import org.apache.maven.model.Parent
import org.junit.Before
import org.junit.Test
import org.sonatype.graven.GravenModelTestSupport
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
    void setUp() {
        writer = new GravenModelWriter()
    }

    @Test
    void test1() {
        def model = new Model(parent: new Parent(groupId: "a", artifactId: "b", version: "c"))

        def buff = new StringWriter()
        writer.write(buff, null, model)

        println(buff)

        def expected = load("test2.groovy")
        assertEquals(expected, buff.toString())
    }
}