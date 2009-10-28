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

import org.apache.maven.model.Model;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.util.List;

/**
 * Executes registered {@link ExecuteTask}s.
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

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter expression="${taskId}"
     * @required
     */
    private String taskId;

    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        Model model = project.getModel();
        log.debug("Executing task '" + taskId + "' for model: " + model.getId());

        assert manager != null;
        List<ExecuteTask> tasks = manager.getTasks(model);

        ExecuteContext ctx = null; // FIXME:

        for (ExecuteTask task : tasks) {
            if (taskId.equals(task.getId())) {
                log.debug("Executing task: " + task);

                try {
                    task.execute(ctx);
                }
                catch (Exception e) {
                    throw new MojoExecutionException(e.getMessage(), e);
                }

                return;
            }
        }

        throw new MojoFailureException("Unable to find task for id: " + taskId);
    }
}