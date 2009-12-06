package org.sonatype.maven.polyglot.clojure;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.PlexusTestCase;
import org.junit.Test;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {@link ClojureModelReader}.
 *
 * @author <a href="mailto:mark@derricutt.com">Mark Derricutt</a>
 */
public class ClojureModelReaderTest
    extends PlexusTestCase
{
    private ModelReader reader;

    @Test
    public void testReading() throws Exception {

        reader = lookup(ModelReader.class, "clojure");

        final String source = "test1.clj";
        URL input = getClass().getResource(source);
        assertNotNull(input);

        Map options = new HashMap();
        options.put(ModelProcessor.LOCATION, input);
        options.put(ModelProcessor.SOURCE, source);
        Model model = reader.read(input.openStream(), options);
        assertNotNull(model);

        System.out.println(model.getModelVersion());

        assertEquals("UTF-8", model.getModelEncoding());
        assertEquals("jar", model.getPackaging());
        assertEquals("4.0.0", model.getModelVersion());
        assertEquals("a", model.getGroupId());
        assertEquals("b", model.getArtifactId());
        assertEquals("c", model.getVersion());

        assertNotNull(model.getDependencies());

        Dependency dependency = model.getDependencies().get(0);

        assertEquals("org.clojure", dependency.getGroupId());

    }

}
