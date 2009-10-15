package org.sonatype.maven.polyglot

import org.apache.maven.model.io.DefaultModelReader
import org.codehaus.plexus.PlexusTestCase
import org.junit.Before
import org.junit.Test
import org.sonatype.maven.polyglot.PolyglotModelManager
import static org.apache.maven.model.building.ModelProcessor.*

/**
 * Tests for {@link org.sonatype.maven.polyglot.PolyglotModelManager}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class PolyglotModelManagerTest
    extends PlexusTestCase
{
    private org.sonatype.maven.polyglot.PolyglotModelManager manager

    @Before
    void setUp() {
        manager = lookup(org.sonatype.maven.polyglot.PolyglotModelManager.class)
    }

    private void expectReader(def key, def value, def type) {
        def options = [:]
        options.put(key, value)

        def reader = manager.getReaderFor(options)
        assertNotNull(reader)
        assertEquals(type, reader.getClass())
    }

    @Test
    void testAcceptLocationXml() {
        expectReader(LOCATION, "foo.xml", DefaultModelReader)
    }

    @Test
    void testAcceptLocationPom() {
        expectReader(LOCATION, "foo.pom", DefaultModelReader)
    }

    @Test
    void testAcceptKeyXml() {
        expectReader("xml:4.0.0", "xml:4.0.0", DefaultModelReader)
    }
}
