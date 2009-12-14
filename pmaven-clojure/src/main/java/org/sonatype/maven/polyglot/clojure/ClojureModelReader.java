package org.sonatype.maven.polyglot.clojure;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.sonatype.maven.polyglot.execute.ExecuteManager;
import org.sonatype.maven.polyglot.io.ModelReaderSupport;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

/**
 * Reads a <tt>pom.clj</tt> and transforms into a Maven {@link Model}.
 *
 * @author <a href="mailto:mark@derricutt.com">Mark Derricutt</a>
 *
 * @since 1.0
 */
@Component(role=ModelReader.class, hint="clojure")
public class ClojureModelReader
    extends ModelReaderSupport
{
    @Requirement
    protected Logger log;

    @Requirement
    private ModelBuilder builder;

    @Requirement
    private ExecuteManager executeManager;

    public Model read(final Reader input, final Map<String,?> options) throws IOException {
        assert input != null;

        Model model;
        try {

            Object location = options.containsKey(ModelProcessor.LOCATION) ? options.get(ModelProcessor.LOCATION) : "";
            Object source = options.containsKey(ModelProcessor.SOURCE) ? options.get(ModelProcessor.SOURCE) : "";

            clojure.lang.Compiler.load(new InputStreamReader(ClojureModelReader.class.getResourceAsStream("clojuremodel.clj")));


            model = (Model) clojure.lang.Compiler.load(input, location.toString(), source.toString());

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new IOException(e);
        }

        return model;
    }

}
