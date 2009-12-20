package org.sonatype.maven.polyglot.clojure;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
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


    @Test
    public void testReading() throws Exception {

        Model model = readClojureModel("test1.clj");
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

        assertNotNull(model.getBuild());
        assertNotNull(model.getBuild().getPlugins());
        assertEquals(false, model.getBuild().getPlugins().isEmpty());

        assertEquals(2, model.getBuild().getPlugins().size());

        Plugin plugin = model.getBuild().getPlugins().get(1);

        assertNotNull(plugin);
        assertEquals("clojure-maven-plugin", plugin.getArtifactId());
        assertEquals("1.2-SNAPSHOT", plugin.getVersion());

    }

    @Test
    public void testScripted() throws Exception {

        Model model = readClojureModel("test2.clj");
        assertNotNull(model);

        boolean hasTestNg = false;
        for (Dependency dependency : model.getDependencies()) {
            if ("testng".equals(dependency.getArtifactId())) {
                hasTestNg = true;
            }
        }

        assertEquals(true, hasTestNg);



    }

    private Model readClojureModel(final String sourceFile) throws Exception {
        ModelReader reader = lookup(ModelReader.class, "clojure");

        URL input = getClass().getResource(sourceFile);
        assertNotNull(input);

        Map options = new HashMap();
        options.put(ModelProcessor.LOCATION, input);
        options.put(ModelProcessor.SOURCE, sourceFile);
        Model model = reader.read(input.openStream(), options);
        return model;
    }

}
