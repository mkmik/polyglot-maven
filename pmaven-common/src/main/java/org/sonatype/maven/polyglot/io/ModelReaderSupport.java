package org.sonatype.maven.polyglot.io;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.util.IOUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

/**
 * Support for {@link ModelReader} implementations.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public abstract class ModelReaderSupport
    implements ModelReader
{
    public Model read(final File input, final  Map<String,?> options) throws IOException, ModelParseException {
        Model model;

        Reader reader = new BufferedReader(new FileReader(input));
        try {
            model = read(reader, options);
            model.setPomFile(input);
        }
        finally {
            IOUtil.close(reader);
        }
        return model;
    }

    public Model read(final InputStream input, final Map<String,?> options) throws IOException, ModelParseException {
        return read(new InputStreamReader(input), options);
    }
}