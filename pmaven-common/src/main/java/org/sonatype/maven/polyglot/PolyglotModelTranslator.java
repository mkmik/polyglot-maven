package org.sonatype.maven.polyglot;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Translates between polyglot model formats.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
@Component(role=PolyglotModelTranslator.class)
public class PolyglotModelTranslator
{
    @Requirement
    private PolyglotModelManager manager;

    public void translate(final File input, final File output) throws IOException, ModelParseException {
        assert input != null;
        assert output != null;

        Map<String,Object> inputOptions = new HashMap<String,Object>();
        inputOptions.put(ModelProcessor.LOCATION, input.getPath());

        Map<String,Object> outputOptions = new HashMap<String,Object>();
        outputOptions.put(ModelProcessor.LOCATION, output.getPath());

        translate(input, inputOptions, output, outputOptions);
    }

    public void translate(final File input, final Map<String,?> inputOptions, final File output, final Map<String,?> outputOptions) throws IOException, ModelParseException {
        assert input != null;
        assert output != null;

        ModelReader reader = manager.getReaderFor(inputOptions);
        Model model = reader.read(input, inputOptions);

        ModelWriter writer = manager.getWriterFor(outputOptions);
        writer.write(output, (Map<String, Object>) outputOptions, model);
    }

    public void translate(final URL input, final URL output) throws IOException, ModelParseException {
        assert input != null;
        assert output != null;

        Map<String,Object> inputOptions = new HashMap<String,Object>();
        inputOptions.put(ModelProcessor.LOCATION, input.toExternalForm());

        Map<String,Object> outputOptions = new HashMap<String,Object>();
        outputOptions.put(ModelProcessor.LOCATION, output.toExternalForm());

        translate(input, inputOptions, output, outputOptions);
    }

    public void translate(final URL input, final Map<String,?> inputOptions, final URL output, final Map<String,?> outputOptions) throws IOException, ModelParseException {
        assert input != null;
        assert output != null;

        OutputStream out;
        if (output.getProtocol().equals("file")) {
            File file = new File(output.getPath());
            out = new BufferedOutputStream(new FileOutputStream(file));
        }
        else {
            out = output.openConnection().getOutputStream();
        }

        try {
            translate(input.openStream(), inputOptions, out, outputOptions);
        }
        finally {
            out.flush();
        }
    }

    public void translate(final InputStream input, final Map<String,?> inputOptions, final OutputStream output, final Map<String,?> outputOptions) throws IOException, ModelParseException {
        assert input != null;
        assert output != null;

        ModelReader reader = manager.getReaderFor(inputOptions);
        Model model = reader.read(input, inputOptions);

        ModelWriter writer = manager.getWriterFor(outputOptions);
        writer.write(output, (Map<String, Object>) outputOptions, model);
    }

    public void translate(final Reader input, final Map<String,?> inputOptions, final Writer output, final Map<String,?> outputOptions) throws IOException, ModelParseException {
        assert input != null;
        assert output != null;

        ModelReader reader = manager.getReaderFor(inputOptions);
        Model model = reader.read(input, inputOptions);

        ModelWriter writer = manager.getWriterFor(outputOptions);
        writer.write(output, (Map<String, Object>) outputOptions, model);
    }
}