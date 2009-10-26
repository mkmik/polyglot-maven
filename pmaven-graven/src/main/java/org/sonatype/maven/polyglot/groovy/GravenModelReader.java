package org.sonatype.maven.polyglot.groovy;

import groovy.lang.Script;
import groovy.lang.GroovyShell;
import groovy.xml.MarkupBuilder;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.io.DefaultModelReader;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.IOUtil;
import org.sonatype.maven.polyglot.io.ModelReaderSupport;
import org.sonatype.maven.polyglot.groovy.builder.ModelBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * Reads a <tt>pom.groovy</tt> and transforms into a Maven {@link Model}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
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

        String location = null;
        if (options == null) {
            location = String.valueOf(options.get(ModelProcessor.LOCATION));
        }

        GroovyShell shell = new GroovyShell();
        Script script = location != null ? shell.parse(input, location) : shell.parse(input);

        //
        // TODO: Re-introduce $include && Macros
        //

        return (Model) builder.build(script);
    }
}
