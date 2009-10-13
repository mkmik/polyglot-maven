package org.sonatype.graven.polyglot

import org.junit.Before
import org.junit.Test
import org.sonatype.graven.polyglot.PolyglotModelTranslator
import org.codehaus.plexus.PlexusTestCase
import static org.apache.maven.model.building.ModelProcessor.LOCATION

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
    void setUp() throws Exception {
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
    void testXml2Graven() {
        def text = translate("pom1.xml", "pom.groovy")
        println text
        def expect = getClass().getResource("pom1.groovy").text
        assertEquals(expect, text)
    }

    @Test
    void testGraven2Xml() {
        def text = translate("pom1.groovy", "pom.xml")
        println text
        def expect = getClass().getResource("pom1.xml").text
        assertEquals(expect, text)
    }
}
