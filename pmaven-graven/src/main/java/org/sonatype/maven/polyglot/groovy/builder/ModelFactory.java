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

package org.sonatype.maven.polyglot.groovy.builder;

import groovy.util.FactoryBuilderSupport;
import org.apache.maven.model.Model;
import org.sonatype.maven.polyglot.execute.ExecuteManager;
import org.sonatype.maven.polyglot.execute.ExecuteTask;

import java.util.List;
import java.util.Map;

/**
 * ???
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ModelFactory
    extends NamedFactory
{
    public ModelFactory() {
        super("project");
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attrs) throws InstantiationException, IllegalAccessException {
        return new Model();
    }

    @Override
    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        Model model = (Model)node;
        ExecuteManager manager = ((ModelBuilder)builder).getExecuteManager();
        List<ExecuteTask> tasks = ((ModelBuilder) builder).getTasks();

        // System.out.println("Adding execute tasks for model: " + model.getId() + " (" + this + ")");

        manager.register(model, tasks);
        
        // Reset the tasks list for sanity
        tasks.clear();
    }
}