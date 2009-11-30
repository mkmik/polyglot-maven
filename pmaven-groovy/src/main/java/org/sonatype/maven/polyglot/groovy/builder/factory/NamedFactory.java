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

package org.sonatype.maven.polyglot.groovy.builder.factory;

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.util.List;

/**
 * Support for named factories.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public abstract class NamedFactory
    extends AbstractFactory
{
    private final String name;

    protected NamedFactory(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof List) {
            ((List)parent).add(child);
        }
        else {
            InvokerHelper.setProperty(parent, getName(), child);
        }
    }

    protected static class NodeValueParseException
        extends IllegalArgumentException
    {
        public NodeValueParseException(final NamedFactory factory, final Object value) {
            super("Unable to parse " + factory.getName() + " for: " + value + " (" + value.getClass() + ")");
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "name='" + name + '\'' +
            '}';
    }
}