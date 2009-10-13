package org.sonatype.graven

import org.junit.Before
import org.junit.Test
import org.sonatype.graven.GravenModelReader
import org.sonatype.graven.GravenModelTestSupport
import static org.junit.Assert.*

/**
 * Tests for {@link GravenModelReader}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class GravenModelReaderTest
    extends GravenModelTestSupport
{
    private GravenModelReader reader

    @Before
    void setUp() {
        reader = new GravenModelReader()
    }

    @Test
    void test1() {
        def input = getClass().getResource("test1.groovy")
        assertNotNull(input)

        def model = reader.read(input.openStream(), null)
        assertNotNull(model)

        dump(model)

        def parent = model.parent
        assertNotNull(parent)
        
        assertEquals("a", parent.groupId)
        assertEquals("b", parent.artifactId)
        assertEquals("c", parent.version)
        assertEquals("../pom.xml", parent.relativePath)
    }

    @Test
    void test2() {
        def input = getClass().getResource("test3.groovy")
        assertNotNull(input)

        def model = reader.read(input.openStream(), null)
        assertNotNull(model)

        dump(model)

        def parent = model.parent
        assertNotNull(parent)

        assertEquals("a", parent.groupId)
        assertEquals("b", parent.artifactId)
        assertEquals("c", parent.version)
        assertEquals("../pom.xml", parent.relativePath)
    }
}
