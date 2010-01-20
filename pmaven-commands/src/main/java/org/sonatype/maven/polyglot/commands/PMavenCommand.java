/*
 * Sonatype Maven Shell (TM) Commercial Version.
 *
 * Copyright (c) 2009 Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at http://www.sonatype.com/products/mvnsh/attributions/.
 * "Sonatype" and "Sonatype Nexus" are trademarks of Sonatype, Inc.
 */

package org.sonatype.maven.polyglot.commands;

import com.google.inject.Inject;
import org.sonatype.gshell.command.Command;
import org.sonatype.maven.shell.commands.maven.MavenCommand;
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

        // TODO: Hookup the correct model processor

        // TODO: Hookup i18n to re-use the MavenCommand.properties
    }
}