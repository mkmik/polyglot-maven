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
import groovy.util.Node;
import groovy.util.NodeBuilder;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.util.List;
import java.util.Map;

/**
 * Builds object nodes.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class ObjectFactory
    extends NamedFactory
{
    public ObjectFactory(final String name) {
        super(name);
    }

    @Override
    public boolean isHandlesNodeChildren() {
        return true;
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attrs) throws InstantiationException, IllegalAccessException {
        return new Xpp3Dom(getName());
    }

    @Override
    public boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure content) {
        Xpp3Dom dom = (Xpp3Dom)node;

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

        for (Node child : (List<Node>) root.children()) {
            dom.addChild(nodeToXpp3(child));
        }

        return false;
    }

    private Xpp3Dom nodeToXpp3(final Node node) {
        Xpp3Dom dom = new Xpp3Dom((String)node.name());

        Object value = node.value();
        if (value instanceof String) {
            dom.setValue(String.valueOf(value));
        }

        Map attrs = node.attributes();
        for (Object key : attrs.keySet()) {
            dom.setAttribute(String.valueOf(key), String.valueOf(attrs.get(key)));
        }

        for (Object child : node.children()) {
            if (child instanceof Node) {
                dom.addChild(nodeToXpp3((Node)child));
            }
        }

        return dom;
    }
}