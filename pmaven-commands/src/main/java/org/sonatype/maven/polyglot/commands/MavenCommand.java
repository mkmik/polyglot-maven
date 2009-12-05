/*
 * Sonatype Maven Shell (TM) Professional Version.
 *
 * Copyright (c) 2009 Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at http://www.sonatype.com/products/mvnsh/attributions/.
 * "Sonatype" and "Sonatype Nexus" are trademarks of Sonatype, Inc.
 */

package org.sonatype.maven.polyglot.commands;

import com.google.inject.Inject;
import jline.Terminal;
import org.apache.maven.cli.PolyglotMavenCli;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.util.StringUtils;
import org.sonatype.grrrowl.Growler;
import org.sonatype.gshell.VariableNames;
import org.sonatype.gshell.Variables;
import org.sonatype.gshell.command.Command;
import org.sonatype.gshell.command.CommandActionSupport;
import org.sonatype.gshell.command.CommandContext;
import org.sonatype.gshell.command.IO;
import org.sonatype.gshell.plexus.PlexusRuntime;
import org.sonatype.gshell.util.Arguments;
import org.sonatype.gshell.util.Strings;
import org.sonatype.gshell.util.cli.OpaqueArguments;
import org.sonatype.gshell.io.StreamJack;
import org.sonatype.gshell.io.StreamSet;
import org.sonatype.gshell.util.pref.Preference;

/**
 * Execute Maven.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 0.7
 */
@Command(name="mvn")
public class MavenCommand
    extends CommandActionSupport
    implements OpaqueArguments, VariableNames
{
    private static enum Notifications
    {
        BUILD_PASSED, BUILD_FAILED
    }

    private final PlexusRuntime plexus;

    @Preference
    private boolean growl = true;

    @Inject
    public MavenCommand(final PlexusRuntime plexus) {
        assert plexus != null;
        this.plexus = plexus;
    }

    public Object execute(final CommandContext context) throws Exception {
        assert context != null;
        IO io = context.getIo();

        // Setup Growl
        Growler growler = new Growler("mvn", Notifications.class);
        growler.register();

        //
        // FIXME: Replace with the Embedder
        //

        String[] args = Arguments.toStringArray(context.getArguments());

        // Propagate shell.user.dir to user.dir for MavenCLI
        Variables vars = context.getVariables();
        String dirname = vars.get(SHELL_USER_DIR, String.class);
        System.setProperty("user.dir", dirname);

        log.debug("Invoking maven with args: {}, in dir: {}", StringUtils.join(args, " "), dirname);

        StreamSet current = StreamJack.current();
        StreamSet streams = new StreamSet(current.in, new ColorizingStream(current.out), new ColorizingStream(current.err));
        StreamJack.register(streams);

        int result;
        try {
            ClassWorld classWorld = new ClassWorld("plexus.core", Thread.currentThread().getContextClassLoader());
            result = PolyglotMavenCli.main(args, classWorld);
        }
        finally {
            StreamJack.deregister();

            // HACK: Not sure why, but we need to reset the terminal after some mvn builds
            Terminal term = io.getTerminal();
            term.restore();
            term.init();
        }

        if (result == 0) {
            if (growl) {
                growler.growl(
                    Notifications.BUILD_PASSED,
                    "Build Passed",
                    "Build was successful: " + getName() + " " + Strings.join(args, " "));
            }

            return Result.SUCCESS;
        }
        else if (result == 1) {
            if (growl) {
                growler.growl(
                    Notifications.BUILD_FAILED,
                    "Build Failed",
                    "Build has failed: " + getName() + " " + Strings.join(args, " "));
            }

            return Result.FAILURE;
        }
        else {
            return result;
        }
    }
}