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
 * Builds {@link Parent} nodes.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ParentFactory
    extends NamedFactory
{
    public ParentFactory() {
        super("parent");
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attribs) throws InstantiationException, IllegalAccessException {
        Parent node;

        if (value != null) {
            node = parse(value);

            if (node == null) {
                throw new NodeValueParseException(this, value);
            }
        }
        else {
            node = new Parent();
        }

        return node;
    }

    public static Parent parse(final Object value) {
        assert value != null;

        if (value instanceof String) {
            Parent node = new Parent();
            String[] items = ((String)value).split(":");
            switch (items.length) {
                case 3:
                    node.setGroupId(items[0]);
                    node.setArtifactId(items[1]);
                    node.setVersion(items[2]);
                    return node;
            }
        }

        return null;
    }
}