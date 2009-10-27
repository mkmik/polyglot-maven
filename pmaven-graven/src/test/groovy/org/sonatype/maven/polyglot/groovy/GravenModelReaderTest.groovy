package org.sonatype.maven.polyglot.groovy

import org.apache.maven.model.building.ModelProcessor
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.*
import org.apache.maven.model.io.ModelReader

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
        reader = lookup(ModelReader.class, "graven")
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

    @Test
    void testExecute() {
        def input = getClass().getResource("execute1.groovy")
        assertNotNull(input)

        def options = [:]
        options.put(ModelProcessor.LOCATION, input)
        def model = reader.read(input.openStream(), options)
        assertNotNull(model)

        dump(model)
    }
}
