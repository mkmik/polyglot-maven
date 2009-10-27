/*
 * Copyright (C) 2009 the original author(s).
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

package org.sonatype.maven.polyglot.execute;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.util.List;

/**
 * ???
 *
 * @goal execute
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ExecuteMojo
    extends AbstractMojo
{
    /**
     * @component role="org.sonatype.maven.polyglot.execute.ExecuteManager"
     */
    private ExecuteManager manager;

    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("EXEUTING; w/manager: " + manager);
        System.out.println("containers: " + manager.getContainers());
        
        List<ExecuteContainer> containers = manager.getContainers();

        for (ExecuteContainer container : containers) {
            try {
                container.execute(null);
            }
            catch (Exception e) {
                // HACK: Blah
                e.printStackTrace();
            }
        }
    }
}