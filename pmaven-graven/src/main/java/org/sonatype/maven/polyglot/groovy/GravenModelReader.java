package org.sonatype.maven.polyglot.groovy;

import groovy.lang.GroovyShell;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.IOUtil;
import org.sonatype.maven.polyglot.groovy.builder.ModelBuilder;
import org.sonatype.maven.polyglot.io.ModelReaderSupport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
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
    public Model read(final Reader input, final Map<String,?> options) throws IOException, ModelParseException {
        assert input != null;
        
        // GroovyShell does not support parsing readers, so convert to ByteArrayInputStream
        return read(new ByteArrayInputStream(IOUtil.toByteArray(input)), options);
    }

    @Override
    public Model read(final InputStream input, final Map<String,?> options) throws IOException, ModelParseException {
        assert input != null;

        ModelBuilder builder = new ModelBuilder();
        GroovyShell shell = new GroovyShell();

        ModelLoader loader = new ModelLoader(builder, shell);
        return loader.load(input, options);
    }
}
