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
import org.codehaus.plexus.component.annotations.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of the {@link ExecuteManager} component.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
@Component(role=ExecuteManager.class, instantiationStrategy="singleton")
public class ExecuteManagerImpl
    implements ExecuteManager
{
    private final Map<String,List<ExecuteTask>> modelTasks = new HashMap<String,List<ExecuteTask>>();

    public void register(final Model model, final List<ExecuteTask> tasks) {
        assert model != null;
        assert tasks != null;

        // Need to copy the contents to avoid the elements
        List<ExecuteTask> copy = new ArrayList<ExecuteTask>(tasks.size());
        copy.addAll(tasks);
        modelTasks.put(model.getId(), Collections.unmodifiableList(copy));

        // System.out.println("Registered tasks for: " + model.getId() + "=" + tasks);
    }

    public List<ExecuteTask> getTasks(final Model model) {
        assert model != null;

        // System.out.println("Getting tasks for: " + model.getId());
        // System.out.println("All tasks: " + modelTasks);

        List<ExecuteTask> tasks = modelTasks.get(model.getId());
        if (tasks == null) {
            return Collections.emptyList();
        }

        return tasks;
    }
}