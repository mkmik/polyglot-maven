package org.sonatype.graven.polyglot;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;
import org.apache.maven.model.Model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

/**
 * Translates between polyglot model formats.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role=PolyglotModelTranslator.class)
public class PolyglotModelTranslator
{
    @Requirement
    private PolyglotModelManager manager;

    public void translate(final File input, final Map<String,?> inputOptions, final File output, final Map<String,?> outputOptions) throws IOException, ModelParseException {
        assert input != null;
        assert output != null;

        ModelReader reader = manager.getReaderFor(inputOptions);
        ModelWriter writer = manager.getWriterFor(outputOptions);

        Model model = reader.read(input, inputOptions);
        writer.write(output, (Map<String, Object>)outputOptions, model);
    }

    public void translate(final InputStream input, final Map<String,?> inputOptions, final OutputStream output, final Map<String,?> outputOptions) throws IOException, ModelParseException {
        assert input != null;
        assert output != null;

        ModelReader reader = manager.getReaderFor(inputOptions);
        ModelWriter writer = manager.getWriterFor(outputOptions);

        Model model = reader.read(input, inputOptions);
        writer.write(output, (Map<String, Object>)outputOptions, model);
    }

    public void translate(final Reader input, final Map<String,?> inputOptions, final Writer output, final Map<String,?> outputOptions) throws IOException, ModelParseException {
        assert input != null;
        assert output != null;

        ModelReader reader = manager.getReaderFor(inputOptions);
        ModelWriter writer = manager.getWriterFor(outputOptions);

        Model model = reader.read(input, inputOptions);
        writer.write(output, (Map<String, Object>)outputOptions, model);
    }
}