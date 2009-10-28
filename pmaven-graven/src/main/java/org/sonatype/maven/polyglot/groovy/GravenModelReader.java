package org.sonatype.maven.polyglot.groovy;

import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.StackTraceUtils;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.sonatype.maven.polyglot.PolyglotModelUtil;
import org.sonatype.maven.polyglot.execute.ExecuteManager;
import org.sonatype.maven.polyglot.execute.ExecuteTask;
import org.sonatype.maven.polyglot.groovy.builder.ModelBuilder;
import org.sonatype.maven.polyglot.io.ModelReaderSupport;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Reads a <tt>pom.groovy</tt> and transforms into a Maven {@link Model}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
@Component(role=ModelReader.class, hint="graven")
public class GravenModelReader
    extends ModelReaderSupport
{
    @Requirement
    private ModelBuilder builder;

    @Requirement
    private ExecuteManager executeManager;

    public Model read(final Reader input, final Map<String,?> options) throws IOException {
        assert input != null;

        Model model;

        try {
            model = doRead(input, options);
        }
        catch (Throwable t) {
            t = StackTraceUtils.sanitize(t);

            if (t instanceof IOException) {
                throw (IOException)t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException)t;
            }
            if (t instanceof Error) {
                throw (Error)t;
            }
            
            throw new RuntimeException(t);
        }

        // FIXME: Looks like there are cases where the model is loaded more than once

        registerExecuteTasks(model);

        return model;
    }

    private Model doRead(final Reader input, final Map<String,?> options) throws IOException {
        assert input != null;

        GroovyShell shell = new GroovyShell();
        String text = DefaultGroovyMethods.getText(input);
        String location = PolyglotModelUtil.getLocation(options);
        Script script = shell.parse(new GroovyCodeSource(text, location, location));

        /*
        FIXME: Bring this back as pure java
        
        def include = {source ->
            assert source != null

            def include

            // TODO: Support String, support loading from resource

            if (source instanceof Class) {
                include = source.newInstance()
            }
            else if (source instanceof File) {
                include = shell.parse((File)source)
            }
            else if (source instanceof URL) {
                include = shell.parse(((URL)source).openStream())
            }
            else {
                throw new IllegalArgumentException("Invalid include source: $source")
            }

            include.run()

            // Include each closure variable which starts with '$' and curry in the builder
            include.binding.properties.variables.each {
                if (it.value instanceof Closure && it.key.startsWith('$')) {
                    binding.setVariable(it.key, it.value.curry(builder))
                }
            }
        }

        include(Macros)

        binding.setProperty('$include', include)
        */

        assert builder != null;
        return (Model) builder.build(script);
    }

    //
    // FIXME: This should probably be in a util class or handled by the manager directly.
    //

    private void registerExecuteTasks(final Model model) {
        assert model != null;

        assert executeManager != null;
        List<ExecuteTask> tasks = executeManager.getTasks(model);
        if (tasks.isEmpty()) {
            return;
        }

        // System.out.println("Registering tasks for: " + model.getId());

        if (model.getBuild() == null) {
            model.setBuild(new Build());
        }

        // FIMXE: Should not need to hard-code the version here
        Plugin plugin = new Plugin();
        plugin.setGroupId("org.sonatype.pmaven");
        plugin.setArtifactId("pmaven-execute-plugin");
        plugin.setVersion("1.0-SNAPSHOT");
        model.getBuild().addPlugin(plugin);

        List<String> goals = Collections.singletonList("execute");

        for (ExecuteTask task : executeManager.getTasks(model)) {
            // System.out.println("Registering task: " + task);

            String id = task.getId();

            PluginExecution execution = new PluginExecution();
            execution.setId(id);
            execution.setPhase(task.getPhase());
            execution.setGoals(goals);
            
            Xpp3Dom config = new Xpp3Dom("configuration");
            execution.setConfiguration(config);

            Xpp3Dom child = new Xpp3Dom("taskId");
            child.setValue(id);
            config.addChild(child);

            plugin.addExecution(execution);
        }
    }
}
