package org.sonatype.graven.polyglot

import org.junit.Before
import org.junit.Test
import org.sonatype.graven.polyglot.PolyglotModelTranslator
import org.codehaus.plexus.PlexusTestCase
import static org.apache.maven.model.building.ModelProcessor.LOCATION
import org.apache.maven.model.io.DefaultModelReader
import org.sonatype.graven.GravenModelReader

/**
 * Tests for {@link PolyglotModelManager}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class PolyglotModelManagerTest
    extends PlexusTestCase
{
    private PolyglotModelManager manager

    @Before
    void setUp() throws Exception {
        manager = lookup(PolyglotModelManager.class)
    }

    @Test
    void testAcceptLocationXml() {
        def options = [:]
        options.put(LOCATION, "foo.xml")

        def reader = manager.getReaderFor(options)
        assertNotNull(reader)
        assertEquals(DefaultModelReader.class, reader.getClass())
    }

    @Test
    void testAcceptLocationPom() {
        def options = [:]
        options.put(LOCATION, "foo.pom")

        def reader = manager.getReaderFor(options)
        assertNotNull(reader)
        assertEquals(DefaultModelReader.class, reader.getClass())
    }

    @Test
    void testAcceptKeyXml() {
        def options = [:]
        options.put("xml:4.0.0", "xml:4.0.0")

        def reader = manager.getReaderFor(options)
        assertNotNull(reader)
        assertEquals(DefaultModelReader.class, reader.getClass())
    }

    @Test
    void testAcceptLocationGroovy() {
        def options = [:]
        options.put(LOCATION, "foo.groovy")

        def reader = manager.getReaderFor(options)
        assertNotNull(reader)
        assertEquals(GravenModelReader.class, reader.getClass())
    }

    @Test
    void testAcceptLocationGy() {
        def options = [:]
        options.put(LOCATION, "foo.gy")

        def reader = manager.getReaderFor(options)
        assertNotNull(reader)
        assertEquals(GravenModelReader.class, reader.getClass())
    }

    @Test
    void testAcceptKeyGraven() {
        def options = [:]
        options.put("graven:4.0.0", "graven:4.0.0")

        def reader = manager.getReaderFor(options)
        assertNotNull(reader)
        assertEquals(GravenModelReader.class, reader.getClass())
    }
}
