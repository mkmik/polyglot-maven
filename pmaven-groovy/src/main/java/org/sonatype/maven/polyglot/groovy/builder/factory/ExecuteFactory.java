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

package org.sonatype.maven.polyglot.groovy.builder.factory;

import groovy.lang.Closure;
import groovy.util.FactoryBuilderSupport;
import org.apache.maven.model.Build;
import org.sonatype.maven.polyglot.execute.ExecuteTask;
import org.sonatype.maven.polyglot.groovy.builder.ModelBuilder;
import org.sonatype.maven.polyglot.groovy.execute.GroovyExecuteTask;

import java.util.List;
import java.util.Map;

/**
 * Builds {@link GroovyExecuteTask}s.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class ExecuteFactory
    extends NamedFactory
{
    public ExecuteFactory() {
        super("$execute");
    }

    @Override
    public boolean isHandlesNodeChildren() {
        return true;
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attrs) throws InstantiationException, IllegalAccessException {
        return new GroovyExecuteTask(value, attrs);
    }

    @Override
    public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof Build) {
            GroovyExecuteTask task = (GroovyExecuteTask) child;
            List<ExecuteTask> tasks = ((ModelBuilder) builder).getTasks();
            tasks.add(task);
        }
        else {
            throw new IllegalStateException("The " + getName() + " element must only be defined under 'build'");
        }
    }

    @Override
    public boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure content) {
        GroovyExecuteTask task = (GroovyExecuteTask) node;
        task.setClosure(content);
        return false;
    }

    @Override
    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        GroovyExecuteTask task = (GroovyExecuteTask) node;
        if (task.getId() == null) {
            throw new IllegalStateException("Execute task is missing attribute 'id'");
        }
        if (task.getPhase() == null) {
            throw new IllegalStateException("Execute task is missing attribute 'phase'");
        }
    }
}