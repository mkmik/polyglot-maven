package org.sonatype.maven.polyglot

import org.codehaus.plexus.PlexusTestCase
import org.junit.Before
import org.junit.Test
import static org.apache.maven.model.building.ModelProcessor.*

/**
 * Tests for {@link PolyglotModelTranslator}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class PolyglotModelTranslatorTest
    extends PlexusTestCase
{
    private PolyglotModelTranslator translator

    @Before
    void setUp() {
        translator = lookup(PolyglotModelTranslator.class)
    }

    private String translate(final String input, final String output) {
        assertNotNull(input)
        assertNotNull(output)

        def url = getClass().getResource(input)
        assertNotNull(url)
        def inputOptions = [:]
        inputOptions.put(LOCATION, url.path)

        def buff = new StringWriter()
        def outputOptions = [:]
        outputOptions.put(LOCATION, output)

        translator.translate(url.newReader(), inputOptions, buff, outputOptions)

        return buff.toString()
    }

    @Test
    void testXml2Xml() {
        def text = translate("pom1.xml", "pom.xml")
        def expect = getClass().getResource("pom1.xml").text
        assertEquals(expect, text)
    }
}
