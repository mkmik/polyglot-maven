package org.sonatype.graven.cli;

import org.apache.maven.cli.MavenLoggerManager;
import org.apache.maven.cli.PrintStreamLogger;
import org.apache.maven.model.building.ModelProcessor;
import org.codehaus.plexus.ContainerConfiguration;
import org.codehaus.plexus.DefaultContainerConfiguration;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.sonatype.graven.polyglot.PolyglotModelTranslator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Polgyglot model translator.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class PolyglotTranslatorCli
{
    private DefaultPlexusContainer container;

    private PrintStreamLogger logger;

    public PolyglotTranslatorCli(ClassWorld classWorld) {
        if (classWorld == null) {
            classWorld = new ClassWorld("plexus.core", Thread.currentThread().getContextClassLoader());
        }

        try {
            ContainerConfiguration cc = new DefaultContainerConfiguration()
                    .setClassWorld(classWorld)
                    .setName("translator");
            container = new DefaultPlexusContainer(cc);
        }
        catch (PlexusContainerException e) {
            throw new IllegalStateException("Could not start component container: " + e.getMessage(), e);
        }

        logger = new PrintStreamLogger(System.out);

        container.setLoggerManager(new MavenLoggerManager(logger));
    }

    public int run(final String[] args) throws Exception {
        if (args == null || args.length != 2) {
            System.out.println("translator <input-file> <output-file>");
            return -1;
        }

        File input = new File(args[0]).getCanonicalFile();
        Map<String,String> inputOptions = new HashMap<String,String>();
        inputOptions.put(ModelProcessor.LOCATION, input.getPath());
        System.out.println("Input: " + input);

        File output = new File(args[1]).getCanonicalFile();
        Map<String,String> outputOptions = new HashMap<String,String>();
        outputOptions.put(ModelProcessor.LOCATION, output.getPath());
        System.out.println("Output: " + output);

        PolyglotModelTranslator translator = container.lookup(PolyglotModelTranslator.class);

        translator.translate(input, inputOptions, output, outputOptions);

        System.out.println("Done");
        
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        int result = main(args, null);
        System.exit(result);
    }

    public static int main(final String[] args, final ClassWorld classWorld) throws Exception {
        assert classWorld != null;
        return new PolyglotTranslatorCli(classWorld).run(args);
    }
}