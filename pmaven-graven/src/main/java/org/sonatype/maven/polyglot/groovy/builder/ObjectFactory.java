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

/**
 * Builds object nodes.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ObjectFactory
    extends NamedFactory
{
    public ObjectFactory(final String name) {
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
        return new Xpp3Dom(getName());
    }

    @Override
    public boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
        //
        // FIXME:
        //

        NodeBuilder b = new NodeBuilder();
        childContent.setDelegate(b);
        childContent.setResolveStrategy(Closure.DELEGATE_FIRST);
        
        Node root = (Node) b.invokeMethod(getName(), childContent);

        System.out.println("Result: " + root);
        
        return false;
    }
}