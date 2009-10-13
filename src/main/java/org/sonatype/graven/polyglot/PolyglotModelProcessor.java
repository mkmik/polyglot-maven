package org.sonatype.graven.polyglot;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
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
 * Polyglot model processor.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role=ModelProcessor.class, hint="polyglot")
public class PolyglotModelProcessor
    implements ModelProcessor
{
    @Requirement
    private PolyglotModelManager manager;

    public File locatePom(final File dir) {
        assert manager != null;
        return manager.locatePom(dir);
    }

    public Model read(final File input, final  Map<String,?> options) throws IOException, ModelParseException {
        Model model;

        // FIXME: This is never called by Maven.  DefaultModelBuilder.readModel() uses a FileModelSource and that goes
        //        straight to read(IS).  readModel() should be augmented to provide some more context about the source type
        //        via options so that we can correctly determin the type of reader to use

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

    public Model read(final Reader input, final Map<String,?> options) throws IOException, ModelParseException {
        assert manager != null;
        ModelReader reader = manager.getReaderFor(options);
        return reader.read(input, options);
    }
}
