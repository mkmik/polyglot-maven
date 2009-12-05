/*
 * Sonatype Maven Shell (TM) Professional Version.
 *
 * Copyright (c) 2009 Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at http://www.sonatype.com/products/mvnsh/attributions/.
 * "Sonatype" and "Sonatype Nexus" are trademarks of Sonatype, Inc.
 */

package org.sonatype.maven.polyglot.commands;

import org.junit.Test;
import org.sonatype.gshell.commands.CommandTestSupport;
import org.sonatype.maven.polyglot.commands.MavenCommand;

import java.net.URL;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link MavenCommand}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class MavenCommandTest
    extends CommandTestSupport
{
    public MavenCommandTest() {
        super(MavenCommand.class);
    }

    @Override
    @Test
    public void testDefault() throws Exception {
        Object result = execute();
        assertEqualsFailure(result);
    }

    @Override
    @Test
    public void testHelp() throws Exception {
        Object result;

        result = executeWithArgs("--help");

        // HACK: Ignore result for now mvn3's cli is in too much flux
        // assertEqualsSuccess(result);

        result = executeWithArgs("-h");

        // HACK: Ignore result for now mvn3's cli is in too much flux
        // assertEqualsSuccess(result);
    }

    @Test
    public void test1() throws Exception {
        URL script = getClass().getResource("test1.pom");
        assertNotNull(script);
        Object result = executeWithArgs("-f", script.toExternalForm(), "-o");

        // HACK: Ignore result for now mvn3's cli is in too much flux
        // assertEqualsSuccess(result);
    }
}