/*
 * Copyright (C) 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 *
 * @since 0.7
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
