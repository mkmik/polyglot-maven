package org.sonatype.maven.polyglot.cli

import org.apache.maven.model.Model
import org.apache.maven.model.building.ModelProcessor
import org.apache.maven.model.io.ModelWriter
import org.codehaus.plexus.PlexusTestCase
import org.junit.Before
import org.junit.Test
import org.sonatype.maven.polyglot.groovy.GroovyModelWriter
import org.sonatype.maven.polyglot.PolyglotModelManager

/**
 * Tests for {@link PolyglotTranslatorCli}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class PolyglotTranslatorCliTest
    extends PlexusTestCase
{
    private PolyglotModelManager manager;

    private PolyglotTranslatorCli translator;

    // NOTE: Using Groovy, as XML seems to put in encoding sometimes that messes up the tests
    private ModelWriter writer = new GroovyModelWriter()

    @Before
    void setUp() {
        manager = lookup(PolyglotModelManager.class)
        translator = new PolyglotTranslatorCli();
    }

    private void translate(String input, String ext, String expected) {
        println "Translating $input, $ext, $expected"

        def url = getClass().getResource(input)
        assertNotNull(url)

        println "Input text:\n${url.text}"

        def file = File.createTempFile("pom", ext)
        file.deleteOnExit()
        try {
            translator.translate(url, file.toURI().toURL())
            println "Translated text:\n${file.text}"

            url = getClass().getResource(expected)
            assertNotNull(url)

            def expectedModel = loadModel(url.openStream(), expected)
            def actualModel = loadModel(file.newInputStream(), file.name)

            assertModelEquals(expectedModel, actualModel)
        }
        finally {
            file.delete();
        }
    }

    private Model loadModel(final InputStream input, final String location) {
        def options = [:]
        options.put(ModelProcessor.LOCATION, location)
        def reader = manager.getReaderFor(options);
        return reader.read(input, options)
    }

    private void assertModelEquals(final Model expected, final Model actual) {
        def xml1 = new StringWriter()
        writer.write(xml1, null, expected)
        def xml2 = new StringWriter()
        writer.write(xml2, null, actual)
        assertEquals(xml1.toString(), xml2.toString())
    }

    @Test
    void testFormatInterchange() {
        def formats = [
            'xml',
            'groovy',
            'yml',
//            'scala',
            'clj'
        ]

        for (source in formats) {
            for (target in formats) {
                println "Testing $source -> $target"
                translate("pom1.$source", ".$target", "pom1.$target")
            }
        }
    }
}
