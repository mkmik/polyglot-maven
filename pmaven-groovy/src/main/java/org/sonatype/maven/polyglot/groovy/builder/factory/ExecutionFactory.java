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

import groovy.util.FactoryBuilderSupport;
import org.apache.maven.model.PluginExecution;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Builds {@link org.apache.maven.model.PluginExecution} nodes.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.0
 */
public class ExecutionFactory
    extends NamedFactory
{
    public ExecutionFactory() {
        super("execution");
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attrs) throws InstantiationException, IllegalAccessException {
        return new PluginExecution();
    }

    @Override
    public boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object current, Map attrs) {
        PluginExecution node = (PluginExecution) current;

        // Custom handling for 'goal' and 'goals' attributes
        if (attrs.containsKey("goal")) {
            Object value = attrs.get("goal");
            node.setGoals(Collections.singletonList(String.valueOf(value)));
            attrs.remove("goal");
        }
        else if (attrs.containsKey("goals")) {
            Object value = attrs.get("goals");
            if (value instanceof String) {
                node.setGoals(Collections.singletonList(String.valueOf(value)));
            }
            else if (value instanceof List) {
                node.setGoals((List) value);
            }
            attrs.remove("goals");
        }

        return true;
    }
}