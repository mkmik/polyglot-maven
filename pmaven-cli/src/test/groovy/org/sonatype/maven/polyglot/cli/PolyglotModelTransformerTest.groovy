package org.sonatype.maven.polyglot.cli

import org.apache.maven.model.Model
import org.apache.maven.model.building.ModelProcessor
import org.apache.maven.model.io.ModelWriter
import org.apache.maven.model.io.DefaultModelWriter
import org.codehaus.plexus.PlexusTestCase
import org.junit.Before
import org.junit.Test
import org.sonatype.maven.polyglot.PolyglotModelManager
import org.junit.Ignore

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

    private ModelWriter writer = new DefaultModelWriter()

    @Before
    void setUp() {
        manager = lookup(PolyglotModelManager.class)
        translator = new PolyglotTranslatorCli();
    }

    private void translate(String input, String ext, String expected) {
        println "Translating $input, $ext, $expected"

        def url = getClass().getResource(input)
        assertNotNull(url)

        println "Input url:\n${url}"
        println "Input text:\n${url.text}"

        def file = File.createTempFile("pom", ext)
        file.deleteOnExit()
        try {
            translator.translate(url, file.toURI().toURL())
            println "Translated text file url:\n ${file.toURI().toURL()}"
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
        //...strips all whitespace and canonicalizes XML documents...
        def c11r = new NoWhitespaceXMLCanonicalizer();
        
        def swxml1 = new StringWriter()
        writer.write(swxml1, null, expected)
        def xml1 = c11r.transform(swxml1.toString())
        
        def swxml2 = new StringWriter()
        writer.write(swxml2, null, actual)
        def xml2 = c11r.transform(swxml2.toString());
        
        assertEquals(xml1, xml2)
    }

    @Test
    @Ignore // FIXME: This test is fundamentally broken
    void testFormatInterchange() {
        def formats = [
            'xml',
            'groovy',
            'yml',
//            'scala',
//            'clj'
        ]

        for (source in formats) {
            for (target in formats) {
                println "Testing $source -> $target"
                translate("pom1.$source", ".$target", "pom1.$target")
            }
        }
    }
}
