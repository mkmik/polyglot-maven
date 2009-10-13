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
    void setUp() {
        manager = lookup(PolyglotModelManager.class)
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

    @Test
    void testAcceptLocationGroovy() {
        expectReader(LOCATION, "foo.groovy", GravenModelReader)
    }

    @Test
    void testAcceptLocationGy() {
        expectReader(LOCATION, "foo.gy", GravenModelReader)
    }

    @Test
    void testAcceptKeyGraven() {
        expectReader("graven:4.0.0", "graven:4.0.0", GravenModelReader)
    }
}
