package org.sonatype.maven.polyglot.cli

import org.codehaus.plexus.PlexusTestCase
import org.junit.Before
import org.junit.Test
import org.sonatype.maven.polyglot.cli.PolyglotTranslatorCli

/**
 * Tests for {@link PolyglotTranslatorCli}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class PolyglotTranslatorCliTest
    extends PlexusTestCase
{
    private PolyglotTranslatorCli translator;

    @Before
    void setUp() {
        translator = new PolyglotTranslatorCli();
    }

    private void translate(String input, String ext, String expected) {
        def url = getClass().getResource(input)
        def file = File.createTempFile("pom", ext)
        file.deleteOnExit()
        try {
            translator.translate(url, file.toURI().toURL())
            assertEqualsText(getClass().getResource(expected).text, file.text)
        }
        finally {
            file.delete();
        }
    }

    @Test
    void testXml2Xml() {
        translate("pom1.xml", ".xml", "pom1.xml")
    }

    @Test
    void testXml2Pom() {
        translate("pom1.xml", ".pom", "pom1.pom")
    }

    @Test
    void testPom2Pom() {
        translate("pom1.pom", ".pom", "pom1.pom")
    }

    @Test
    void testXml2Groovy() {
        translate("pom1.xml", ".groovy", "pom1.groovy")
    }

    @Test
    void testGroovy2Groovy() {
        translate("pom1.groovy", ".groovy", "pom1.groovy")
    }

    @Test
    void testGroovy2Xml() {
        translate("pom1.groovy", ".xml", "pom1.xml")
    }

    private void assertEqualsText( String expected, String actual ) {
        def text = actual.replaceAll( "(\r\n)|(\r)|(\n)", "\n" )
        def expect = expected.replaceAll( "(\r\n)|(\r)|(\n)", "\n" )
        assertEquals(expect, text)
    }

}
