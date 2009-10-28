package org.sonatype.maven.polyglot.groovy;

import groovy.lang.GroovyShell;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.apache.maven.plugin.lifecycle.Execution;
import org.codehaus.groovy.runtime.StackTraceUtils;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.util.IOUtil;
import org.sonatype.maven.polyglot.execute.ExecuteContainer;
import org.sonatype.maven.polyglot.execute.ExecuteManager;
import org.sonatype.maven.polyglot.groovy.builder.ModelBuilder;
import org.sonatype.maven.polyglot.io.ModelReaderSupport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
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
        
        // GroovyShell does not support parsing readers, so convert to ByteArrayInputStream
        return read(new ByteArrayInputStream(IOUtil.toByteArray(input)), options);
    }

    @Override
    public Model read(final InputStream input, final Map<String,?> options) throws IOException {
        assert input != null;

        try {
            return doRead(input, options);
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
            
            throw new IOException(t);
        }
    }

    private Model doRead(final InputStream input, final Map<String,?> options) throws IOException {
        assert input != null;

        GroovyShell shell = new GroovyShell();
        assert builder != null;
        ModelLoader loader = new ModelLoader(builder, shell);
        Model model = loader.load(input, options);

        assert executeManager != null;
        for (ExecuteContainer execute : executeManager.getContainers()) {
            if (model.getBuild() == null) {
                model.setBuild(new Build());
            }

            Plugin plugin = new Plugin();
            plugin.setGroupId("org.sonatype.pmaven");
            plugin.setArtifactId("pmaven-execute-plugin");
            plugin.setVersion("1.0-SNAPSHOT");
            model.getBuild().addPlugin(plugin);

            PluginExecution execution = new PluginExecution();
            execution.setGoals(Collections.singletonList("execute"));
            execution.setPhase(execute.getPhase());
            // execution.setConfiguration();

            plugin.addExecution(execution);
        }

        return model;
    }
}
