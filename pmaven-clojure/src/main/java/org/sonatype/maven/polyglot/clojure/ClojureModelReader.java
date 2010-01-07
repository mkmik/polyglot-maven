package org.sonatype.maven.polyglot.clojure;

import clojure.lang.*;
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
import java.io.StringReader;
import java.util.Map;

import static clojure.lang.RT.seq;

/**
 * Reads a <tt>pom.clj</tt> and transforms into a Maven {@link Model}.
 *
 * @author <a href="mailto:mark@derricutt.com">Mark Derricutt</a>
 * @since 0.7
 */
@Component(role = ModelReader.class, hint = "clojure")
public class ClojureModelReader extends ModelReaderSupport {
    private static final Symbol USER = Symbol.create("user");
    private static final Symbol CLOJURE = Symbol.create("clojure.core");
    private static final Var in_ns = RT.var("clojure.core", "in-ns");
    private static final Var refer = RT.var("clojure.core", "refer");

    @Requirement
    protected Logger log;

    @Requirement
    private ModelBuilder builder;

    @Requirement
    private ExecuteManager executeManager;

    public Model read(final Reader input, final Map<String, ?> options) throws IOException {
        assert input != null;

        Model model;
        try {

            Object location = options.containsKey(ModelProcessor.LOCATION) ? options.get(ModelProcessor.LOCATION) : "";
            Object source = options.containsKey(ModelProcessor.SOURCE) ? options.get(ModelProcessor.SOURCE) : "";

            final String bootstrapClojureDsl = "" +
                    "(use (quote org.sonatype.maven.polyglot.clojure.dsl.reference))" +
                    "(use (quote org.sonatype.maven.polyglot.clojure.dsl.dependency))" +
                    "(use (quote org.sonatype.maven.polyglot.clojure.dsl.plugin))" +
                    "(use (quote org.sonatype.maven.polyglot.clojure.dsl.defaults))" +
                    "(use (quote org.sonatype.maven.polyglot.clojure.dsl.project))";

            clojure.lang.Compiler.load(new StringReader(bootstrapClojureDsl));
            clojure.lang.Compiler.load(input, location.toString(), source.toString());

            Atom project = (Atom) RT.var("org.sonatype.maven.polyglot.clojure.dsl.project", "*PROJECT*").get();

            model = (Model) project.deref();


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new IOException(e);
        }

        return model;
    }

}
