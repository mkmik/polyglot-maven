package org.sonatype.maven.graven;

import groovy.lang.Script;
import groovy.xml.MarkupBuilder;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.DefaultModelReader;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.IOUtil;
import org.sonatype.maven.graven.ScriptFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * Reads a <tt>pom.groovy</tt> and transforms into a Maven {@link Model}.
 *
 * Currently this implementation is very naive, uses a {@link MarkupBuilder} from a parsed
 * {@link Script} to generate XML, which is then transformed into a {@link Model}
 * using the {@link DefaultModelReader}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role=ModelReader.class, hint="graven")
public class GravenModelReader
    implements ModelReader
{
    public Model read(final File file, final Map<String,?> options) throws IOException, ModelParseException {
        assert file != null;

        Model model;
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        try {
            model = read(in, options);
            model.setPomFile(file);
        }
        finally {
            IOUtil.close(in);
        }
        return model;
    }

    public Model read(final Reader input, final Map<String,?> options) throws IOException, ModelParseException {
        assert input != null;
        
        // GroovyShell does not support parsing readers, so convert to ByteArrayInputStream
        return read(new ByteArrayInputStream(IOUtil.toByteArray(input)), options);
    }

    public Model read(final InputStream input, final Map<String,?> options) throws IOException, ModelParseException {
        assert input != null;

        StringWriter buff = new StringWriter();
        Script script = ScriptFactory.create(input, buff, options);
        script.run();

        return transform(buff, options);
    }

    private Model transform(final StringWriter input, final Map<String,?> options) throws IOException {
        assert input != null;
        ModelReader reader = new DefaultModelReader();
        return reader.read(new StringReader(input.toString()), options);
    }
}
