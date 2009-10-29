package org.sonatype.maven.polyglot.io;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.util.IOUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * Support for {@link ModelWriter} implementations.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public abstract class ModelWriterSupport
    implements ModelWriter
{
    public void write(final File file, final Map<String,Object> options, final Model model) throws IOException {
        assert file != null;
        assert model != null;

        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        try {
            write(out, options, model);
            out.flush();
        }
        finally {
            IOUtil.close(out);
        }
    }

    public void write(final OutputStream output, final Map<String,Object> options, final Model model) throws IOException {
        assert output != null;
        assert model != null;

        write(new OutputStreamWriter(output), options, model);
    }    
}