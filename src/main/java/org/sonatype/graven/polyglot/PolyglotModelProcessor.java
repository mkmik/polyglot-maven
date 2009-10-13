package org.sonatype.graven.polyglot;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.BufferedReader;
import java.util.Map;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.io.DefaultModelReader;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.locator.ModelLocator;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.util.IOUtil;
import org.sonatype.graven.GravenModelReader;

/**
 * ???
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role=ModelProcessor.class, hint="polyglot")
public class PolyglotModelProcessor
    implements ModelProcessor
{
    @Requirement(hint="graven")
    private ModelReader gravenModelReader;

    @Requirement(hint="default")
    private ModelReader xmlModelReader;

    @Requirement(hint="polyglot")
    private ModelLocator modelLocator;

    public PolyglotModelProcessor() {
        // HACK: This component is currently wedged into the container so it does not get requirements processed

        if (gravenModelReader == null) {
            gravenModelReader = new GravenModelReader();
        }
        if (xmlModelReader == null) {
            xmlModelReader = new DefaultModelReader();
        }
        if (modelLocator == null) {
            modelLocator = new PolyglotModelLocator();
        }
    }
    
    public File locatePom(final File dir) {
        assert dir != null;
        return modelLocator.locatePom(dir);
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
        // System.out.println("Options: " + options);

        if (options.containsKey("xml:4.0.0")) {
            return xmlModelReader.read(input, options);
        }

        String location = (String) options.get(LOCATION);
        // System.out.println("Location: " + location);

        if (location != null) {
            if (location.endsWith(".xml") || location.endsWith(".pom")) {
                return xmlModelReader.read(input, options);
            }
            else if (location.endsWith(".groovy") || location.endsWith(".gy")) {
                return gravenModelReader.read(input, options);
            }

            throw new ModelParseException("Unable to handle input for location: " + location, -1, -1);
        }

        throw new ModelParseException("No context to determine input type", -1, -1);
    }
}
