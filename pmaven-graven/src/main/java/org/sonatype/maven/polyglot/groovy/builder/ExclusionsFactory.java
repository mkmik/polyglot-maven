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
import org.apache.maven.model.Exclusion;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds exclusions nodes.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ExclusionsFactory
    extends ListFactory
{
    public ExclusionsFactory() {
        super("exclusions");
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attrs) throws InstantiationException, IllegalAccessException {
        List node;

        if (value != null) {
            node = parse(value);

            if (node == null) {
                throw new NodeValueParseException(this, value);
            }
        }
        else {
            node = new ArrayList();
        }

        return node;
    }

    public static List parse(final Object value) {
        assert value != null;

        if (value instanceof String) {
            Exclusion child = ExclusionFactory.parse(value);
            if (child != null) {
                List node = new ArrayList();
                node.add(child);
                return node;
            }
        }
        else if (value instanceof List) {
            List node = new ArrayList();
            for (Object item : (List)value) {
                Exclusion child = ExclusionFactory.parse(item);
                if (child == null) {
                    return null;
                }
                node.add(child);
            }
            return node;
        }

        return null;
    }
}