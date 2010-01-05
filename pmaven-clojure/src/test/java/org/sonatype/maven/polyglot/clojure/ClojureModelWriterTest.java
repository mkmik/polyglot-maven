package org.sonatype.maven.polyglot.clojure;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.Test;

import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;


/**
 * Created by IntelliJ IDEA.
 * User: amrk
 * Date: Dec 13, 2009
 * Time: 1:29:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClojureModelWriterTest extends PlexusTestCase {


    @Test
    public void testSimpleDependencyRendering() {

        Dependency dependency = new Dependency();
        dependency.setGroupId("group");
        dependency.setArtifactId("artifact");
        dependency.setVersion("1.0");

        ClojureModelWriter writer = new ClojureModelWriter();

        StringWriter sw = new StringWriter();
        ClojurePrintWriter out = new ClojurePrintWriter(sw);
        writer.buildDependencyString(out, dependency);

        assertThat(sw.getBuffer().toString())
                .isEqualTo("[\"group:artifact:1.0\"]");

    }

    @Test
    public void testClassifiedDependencyRendering() {

        Dependency dependency = new Dependency();
        dependency.setGroupId("group");
        dependency.setArtifactId("artifact");
        dependency.setVersion("1.0");
        dependency.setClassifier("jdk15");

        ClojureModelWriter writer = new ClojureModelWriter();

        StringWriter sw = new StringWriter();
        ClojurePrintWriter out = new ClojurePrintWriter(sw);
        writer.buildDependencyString(out, dependency);

        assertThat(sw.getBuffer().toString())
                .isEqualTo("[\"group:artifact:1.0\" {:classifier \"jdk15\"}]");

    }

    @Test
    public void testDependencyWithExclusionRendering() {

        Dependency dependency = new Dependency();
        dependency.setGroupId("group");
        dependency.setArtifactId("artifact");
        dependency.setVersion("1.0");

        Exclusion exclusion = new Exclusion();
        exclusion.setGroupId("please");
        exclusion.setArtifactId("exclude");

        dependency.getExclusions().add(exclusion);

        ClojureModelWriter writer = new ClojureModelWriter();

        StringWriter sw = new StringWriter();
        ClojurePrintWriter out = new ClojurePrintWriter(sw);
        writer.buildDependencyString(out, dependency);

        assertThat(sw.getBuffer().toString())
                .isEqualTo(
                        "[\"group:artifact:1.0\" {:exclusions [\"please:exclude\"]}]");

    }

    @Test
    public void testDependencyWithMultiExclusionRendering() {

        Dependency dependency = new Dependency();
        dependency.setGroupId("group");
        dependency.setArtifactId("artifact");
        dependency.setVersion("1.0");

        Exclusion exclusion = new Exclusion();
        exclusion.setGroupId("please");
        exclusion.setArtifactId("exclude");

        dependency.getExclusions().add(exclusion);

        exclusion = new Exclusion();
        exclusion.setGroupId("also");
        exclusion.setArtifactId("exclude");

        dependency.getExclusions().add(exclusion);

        ClojureModelWriter writer = new ClojureModelWriter();

        StringWriter sw = new StringWriter();
        ClojurePrintWriter out = new ClojurePrintWriter(sw);
        writer.buildDependencyString(out, dependency);

        assertThat(sw.getBuffer().toString())
                .isEqualTo("" +
                        "[\"group:artifact:1.0\" {:exclusions [\"please:exclude\"\n" +
                        "                                    \"also:exclude\"]}]");

    }

    @Test
    public void testPluginWithConfigurationAndNoExecution() {

        Plugin plugin = new Plugin();

        plugin.setGroupId("group");
        plugin.setArtifactId("artifact");
        plugin.setVersion("1.0");

        Xpp3Dom node = new Xpp3Dom("configuration");
        Xpp3Dom value = new Xpp3Dom("name");
        value.setValue("Mark");
        node.addChild(value);

        plugin.setConfiguration(node);


        ClojureModelWriter writer = new ClojureModelWriter();

        StringWriter sw = new StringWriter();
        ClojurePrintWriter out = new ClojurePrintWriter(sw);
        writer.buildPluginString(out, plugin);

        assertThat(sw.getBuffer().toString())
                .isEqualTo("" +
                        "[\"group:artifact:1.0\" {:configuration {\"name\" \"Mark\"}}]");

    }

    @Test
    public void testModelPrinting() throws Exception {

        Model model = readClojureModel("test1.clj");
        assertNotNull(model);

        ClojureModelWriter writer = new ClojureModelWriter();

        StringWriter sw = new StringWriter();

        writer.write(sw, new HashMap(), model);

        System.out.println(sw.getBuffer().toString());


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
