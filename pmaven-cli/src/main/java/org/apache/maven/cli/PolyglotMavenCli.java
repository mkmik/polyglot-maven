package org.apache.maven.cli;

import org.apache.maven.model.building.ModelProcessor;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.component.composition.CycleDetectedInComponentGraphException;
import org.codehaus.plexus.component.repository.ComponentDescriptor;

/**
 * Polgyglot-aware Maven CLI.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class PolyglotMavenCli
    extends MavenCli
{
    @Override
    protected void customizeContainer(final PlexusContainer container) {
        assert container != null;

        // HACK: Wedge our processor in as the default
        ComponentDescriptor<?> source = container.getComponentDescriptor(ModelProcessor.class.getName(), "polyglot");
        ComponentDescriptor<?> target = container.getComponentDescriptor(ModelProcessor.class.getName());
        target.setImplementation(source.getImplementation());
        target.addRequirements(source.getRequirements());

        try {
            container.addComponentDescriptor(target);
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
        PolyglotMavenCli cli = new PolyglotMavenCli();
        return cli.doMain(new CliRequest(args, classWorld));
    }
}
