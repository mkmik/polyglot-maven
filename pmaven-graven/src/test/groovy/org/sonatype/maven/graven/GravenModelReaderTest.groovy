package org.sonatype.maven.graven

import org.apache.maven.model.building.ModelProcessor
import org.junit.Before
import org.junit.Test
import org.sonatype.maven.polyglot.groovy.GravenModelReader
import org.sonatype.maven.graven.GravenModelTestSupport
import static org.junit.Assert.*
import org.sonatype.maven.polyglot.groovy.GravenModelReader

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
    void testReading() {
        def input = getClass().getResource("test1.groovy")
        assertNotNull(input)

        def options = [:]
        options.put(ModelProcessor.LOCATION, input)
        def model = reader.read(input.openStream(), options)
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
