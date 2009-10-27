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

import groovy.lang.Closure;
import groovy.util.FactoryBuilderSupport;
import org.apache.maven.model.Build;
import org.sonatype.maven.polyglot.execute.ExecuteContainer;

import java.util.Map;

/**
 * ???
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ExecuteFactory
    extends NamedFactory
{
    public ExecuteFactory() {
        super("$execute");
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public boolean isHandlesNodeChildren() {
        return true;
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attrs) throws InstantiationException, IllegalAccessException {
        return new GroovyExecuteContainer(value, attrs);
    }

    @Override
    public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof Build) {
            Build build = (Build)parent;
            // TODO: Attach
        }
        else {
            throw new IllegalStateException("The " + getName() + " element must only be defined under 'build'");
        }
    }

    @Override
    public boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure content) {
        GroovyExecuteContainer container = (GroovyExecuteContainer)node;
        container.setClosure(content);
        return false;
    }

    private static class GroovyExecuteContainer
        implements ExecuteContainer
    {
        private final Object value;

        private final Map attrs;

        private String phase;

        private Closure closure;

        private GroovyExecuteContainer(final Object value, final Map attrs) {
            this.value = value;
            this.attrs = attrs;
        }

        public Object getValue() {
            return value;
        }

        public Map getAttributes() {
            return attrs;
        }

        public String getPhase() {
            return phase;
        }

        public void setPhase(final String phase) {
            this.phase = phase;
        }

        public Closure getClosure() {
            return closure;
        }

        public void setClosure(final Closure closure) {
            this.closure = closure;
        }

        public void execute() throws Exception {
            // TODO:
        }
    }
}