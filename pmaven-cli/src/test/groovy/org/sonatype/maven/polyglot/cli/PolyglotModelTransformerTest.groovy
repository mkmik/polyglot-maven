package org.sonatype.maven.polyglot.cli

import org.apache.maven.model.Model
import org.apache.maven.model.building.ModelProcessor
import org.apache.maven.model.io.DefaultModelWriter
import org.apache.maven.model.io.ModelWriter
import org.codehaus.plexus.PlexusTestCase
import org.junit.Before
import org.junit.Test
import org.sonatype.maven.polyglot.PolyglotModelManager
import org.sonatype.maven.polyglot.cli.PolyglotTranslatorCli

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
        def url = getClass().getResource(input)
        def file = File.createTempFile("pom", ext)
        file.deleteOnExit()
        try {
            translator.translate(url, file.toURI().toURL())

            def expectedModel = loadModel(getClass().getResource(expected).openStream(), expected)
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

    @Test
    void testGroovy2Yaml() {
        translate("pom1.groovy", ".yml", "pom1.yml")
    }

    @Test
    void testXml2Yaml() {
        translate("pom1.xml", ".yml", "pom1.yml")
    }

    @Test
    void testYaml2Xml() {
        translate("pom1.yml", ".xml", "pom1a.xml")
    }

    @Test
    void testYaml2Yaml() {
        translate("pom1.yml", ".yml", "pom1.yml")
    }

    @Test
    void testYaml2Groovy() {
        translate("pom1.yml", ".groovy", "pom1.groovy")
    }
}
