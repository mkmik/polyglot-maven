/*
 * Sonatype Maven Shell (TM) Commercial Version.
 *
 * Copyright (c) 2009 Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at http://www.sonatype.com/products/mvnsh/attributions/.
 * "Sonatype" and "Sonatype Nexus" are trademarks of Sonatype, Inc.
 */

package org.sonatype.maven.polyglot.commands;

import org.junit.Test;
import org.sonatype.gshell.commands.CommandTestSupport;
import org.sonatype.maven.polyglot.commands.TranslateCommand;

import static org.junit.Assert.fail;

/**
 * Tests for the {@link TranslateCommand}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class TranslateCommandTest
    extends CommandTestSupport
{
    public TranslateCommandTest() {
        super(TranslateCommand.class);
    }

    @Override
    @Test
    public void testDefault() throws Exception {
        try {
            super.testDefault();
            fail();
        }
        catch (Exception e) {
            // expected
        }
    }
}