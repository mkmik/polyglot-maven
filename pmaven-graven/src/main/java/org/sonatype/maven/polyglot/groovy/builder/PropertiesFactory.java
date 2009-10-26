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
import groovy.util.NodeBuilder;
import groovy.util.Node;
import groovy.lang.Closure;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.util.Map;
import java.util.ArrayList;
import java.util.Properties;
import java.util.List;

/**
 * Builds properties nodes.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class PropertiesFactory
    extends NamedFactory
{
    public PropertiesFactory(final String name) {
        super(name);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public boolean isHandlesNodeChildren() {
        return true;
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attribs) throws InstantiationException, IllegalAccessException {
        return new Properties();
    }

    @Override
    public boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure content) {
        Properties props = (Properties)node;

        NodeBuilder nodes = new NodeBuilder() {
            @Override
            protected void setClosureDelegate(final Closure c, final Object o) {
                c.setDelegate(this);
                c.setResolveStrategy(Closure.DELEGATE_FIRST);
            }

            @Override
            public void setProperty(final String name, final Object value) {
                this.invokeMethod(name, value);
            }
        };
        
        content.setDelegate(nodes);
        content.setResolveStrategy(Closure.DELEGATE_FIRST);
        Node root = (Node) nodes.invokeMethod(getName(), content);

        for (Node child : (List<Node>)root.value()) {
            merge(props, child, "");
        }

        return false;
    }

    private void merge(Properties props, Node node, String prefix) {
        assert props != null;
        assert node != null;
        assert prefix != null;

        String name = prefix + node.name();

        Object value = node.value();
        if (value instanceof String) {
            props.setProperty(name, String.valueOf(value));
        }

        Map attrs = node.attributes();
        for (Object key : attrs.keySet()) {
            props.setProperty(name + "." + key, String.valueOf(attrs.get(key)));
        }

        for (Object child : node.children()) {
            if (child instanceof Node) {
                merge(props, (Node)child, name + ".");
            }
        }
    }
}