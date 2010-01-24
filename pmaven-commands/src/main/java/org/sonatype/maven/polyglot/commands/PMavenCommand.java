/*
 * Sonatype Maven Shell (TM) Commercial Version.
 *
 * Copyright (c) 2009 Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at http://www.sonatype.com/products/mvnsh/attributions/.
 * "Sonatype" and "Sonatype Nexus" are trademarks of Sonatype, Inc.
 */

package org.sonatype.maven.polyglot.commands;

import com.google.inject.Inject;
import org.apache.maven.model.building.ModelProcessor;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.sonatype.gshell.command.Command;
import org.sonatype.gshell.util.i18n.ResourceBundleMessageSource;
import org.sonatype.maven.shell.commands.maven.MavenCommand;
import org.sonatype.maven.shell.maven.MavenRuntimeConfiguration;
import org.sonatype.maven.shell.maven.MavenSystem;

/**
 * Execute Polyglot Maven.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 0.9
 */
@Command(name="pmvn")
public class PMavenCommand
    extends MavenCommand
{
    @Inject
    public PMavenCommand(final MavenSystem maven) {
        super(maven);
    }

    @Override
    protected ResourceBundleMessageSource createMessages() {
        return new ResourceBundleMessageSource(getClass(), MavenCommand.class);
    }

    @Override
    protected void customize(final MavenRuntimeConfiguration config) {
        assert config != null;

        config.setDelegate(new MavenRuntimeConfiguration.Delegate()
        {
            public void configure(final DefaultPlexusContainer container) throws Exception {
                assert container != null;

                // HACK: Wedge our processor in as the default
                ComponentDescriptor<?> source = container.getComponentDescriptor(ModelProcessor.class.getName(), "polyglot");
                ComponentDescriptor<?> target = container.getComponentDescriptor(ModelProcessor.class.getName());
                target.setImplementation(source.getImplementation());
                target.addRequirements(source.getRequirements());

                container.addComponentDescriptor(target);
            }
        });
    }
}