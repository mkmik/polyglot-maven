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

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.util.Map;
import java.util.List;

/**
 * Builds child nodes.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ChildFactory
    extends NamedFactory
{
    private final Class type;

    public ChildFactory(final String name, final Class type) {
        super(name);
        this.type = type;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attribs) throws InstantiationException, IllegalAccessException {
        return type.newInstance();
    }
}