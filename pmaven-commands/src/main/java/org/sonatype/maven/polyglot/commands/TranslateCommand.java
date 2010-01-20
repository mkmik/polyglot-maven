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
import org.sonatype.gshell.command.CommandActionSupport;
import org.sonatype.gshell.command.CommandContext;
import org.sonatype.gshell.command.IO;
import org.sonatype.gshell.console.completer.FileNameCompleter;
import org.sonatype.gshell.plexus.PlexusRuntime;
import org.sonatype.gshell.util.FileAssert;
import org.sonatype.gshell.util.cli2.Argument;
import org.sonatype.maven.polyglot.PolyglotModelTranslator;

import java.io.File;

/**
 * Translate Maven pom file formats..
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 0.9
 */
@Command(name="translate")
public class TranslateCommand
    extends CommandActionSupport
{
    private final PlexusRuntime plexus;

    @Argument(index = 0, required = true)
    private File source;

    @Argument(index = 1, required = true)
    private File target;

    @Inject
    public TranslateCommand(final PlexusRuntime plexus) {
        assert plexus != null;
        this.plexus = plexus;
    }

    @Inject
    public TranslateCommand installCompleters(final FileNameCompleter c1) {
        assert c1 != null;
        setCompleters(c1, c1, null);
        return this;
    }

    public Object execute(final CommandContext context) throws Exception {
        assert context != null;
        IO io = context.getIo();

        PolyglotModelTranslator translator = plexus.lookup(PolyglotModelTranslator.class);

        new FileAssert(source).exists().isReadable();

        io.info("Translating: {} -> {}", source, target); // TODO: i18n

        translator.translate(source, target);

        return Result.SUCCESS;
    }
}