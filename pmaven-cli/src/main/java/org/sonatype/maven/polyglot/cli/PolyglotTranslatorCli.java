package org.sonatype.maven.polyglot.cli;

import org.apache.maven.model.building.ModelProcessor;
import org.codehaus.plexus.ContainerConfiguration;
import org.codehaus.plexus.DefaultContainerConfiguration;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.util.IOUtil;
import org.sonatype.maven.polyglot.PolyglotModelTranslator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Polgyglot model translator.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class PolyglotTranslatorCli
{
    private final DefaultPlexusContainer container;

    private final PolyglotModelTranslator translator;

    public PolyglotTranslatorCli(ClassWorld classWorld) throws Exception {
        if (classWorld == null) {
            classWorld = new ClassWorld("plexus.core", Thread.currentThread().getContextClassLoader());
        }

        ContainerConfiguration cc = new DefaultContainerConfiguration()
                .setClassWorld(classWorld)
                .setName("translator");
        container = new DefaultPlexusContainer(cc);

        translator = container.lookup(PolyglotModelTranslator.class);
    }

    public PolyglotTranslatorCli() throws Exception {
        this(null);
    }

    public int run(final String[] args) throws Exception {
        if (args == null || args.length != 2) {
            System.out.println("usage: translate <input-file> <output-file>");
            return -1;
        }

        File input = new File(args[0]).getCanonicalFile();
        File output = new File(args[1]).getCanonicalFile();

        System.out.println("Translating " + input + " -> " + output);

        translate(input, output);

        return 0;
    }

    public void translate(final File input, final File output) throws IOException {
        assert input != null;
        assert output != null;

        translate(input.toURI().toURL(), output.toURI().toURL());
    }

    public void translate(final URL input, final URL output) throws IOException {
        assert input != null;
        assert output != null;

        translator.translate(input, output);
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