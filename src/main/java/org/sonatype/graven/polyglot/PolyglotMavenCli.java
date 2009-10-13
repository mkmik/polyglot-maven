package org.sonatype.graven.polyglot;

import org.apache.maven.cli.MavenCli;
import org.apache.maven.model.building.ModelProcessor;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.component.composition.CycleDetectedInComponentGraphException;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

/**
 * ???
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class PolyglotMavenCli
    extends MavenCli
{
    public PolyglotMavenCli(final ClassWorld classWorld) {
        super(classWorld);
    }
    
    public PolyglotMavenCli() {
        super();
    }
    
    @Override
    protected void customizeContainer(final PlexusContainer container) {
        assert container != null;

        // HACK: Wedge our processor in as the default
        ComponentDescriptor<?> cd = container.getComponentDescriptor(ModelProcessor.class.getName());
        cd.setImplementation(PolyglotModelProcessor.class.getName());

        try {
            container.addComponentDescriptor(cd);
        }
        catch (CycleDetectedInComponentGraphException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(final String[] args) {
        int result = main(args, null);
        System.exit(result);
    }

    public static int main(final String[] args, final ClassWorld classWorld) {
        assert classWorld != null;
        PolyglotMavenCli cli = new PolyglotMavenCli(classWorld);
        return cli.doMain(args, null, System.out, System.err);
    }
}
