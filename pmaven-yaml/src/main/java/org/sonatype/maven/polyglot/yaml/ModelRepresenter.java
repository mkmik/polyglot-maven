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

package org.sonatype.maven.polyglot.yaml;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.FieldProperty;
import org.yaml.snakeyaml.introspector.MethodProperty;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tags;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * YAML model representer.
 *
 * @author jvanzyl
 * @author bentmann
 *
 * @since 1.0
 */
class ModelRepresenter
    extends Representer
{
    public ModelRepresenter()
    {
        this.representers.put( null, new RepresentJavaBean() );
        this.representers.put( Xpp3Dom.class, new RepresentXpp3Dom() );
        this.nullRepresenter = new RepresentNull();
    }

    private class RepresentNull
        implements Represent
    {

        public Node representData( Object data )
        {
            return null;
        }

    }

    private class RepresentJavaBean
        implements Represent
    {
        public Node representData( Object data )
        {
            Set<Property> properties;
            try
            {
                properties = getProperties( data.getClass() );
            }
            catch ( IntrospectionException e )
            {
                throw new YAMLException( e );
            }
            Node node = representJavaBean( properties, data );
            return node;
        }

    }

    private class RepresentXpp3Dom
        implements Represent
    {

        public Node representData( Object data )
        {
            return representMapping( Tags.MAP, toMap( (Xpp3Dom) data ), null );
        }

        private Map<String, Object> toMap( Xpp3Dom dom )
        {
            Map<String, Object> map = new LinkedHashMap<String, Object>();

            int n = dom.getChildCount();
            for ( int i = 0; i < n; i++ )
            {
                Xpp3Dom child = dom.getChild( i );
                if ( child.getValue() != null )
                {
                    map.put( child.getName(), child.getValue() );
                }
                else
                {
                    map.put( child.getName(), toMap( child ) );
                }
            }

            return map;
        }

    }

    @Override
    protected Node representMapping(String tag, Map<? extends Object, Object> mapping, Boolean flowStyle) {
        // TODO: skipping empty maps should likely be an option of the dumper (and probably default to true)
        if (mapping.isEmpty()) {
            return null;
        }

        List<NodeTuple> value = new LinkedList<NodeTuple>();
        MappingNode node = new MappingNode(tag, value, flowStyle);
        representedObjects.put(objectToRepresent, node);
        boolean bestStyle = true;
        for (Object itemKey : mapping.keySet()) {
            Object itemValue = mapping.get(itemKey);
            Node nodeKey = representData(itemKey);
            Node nodeValue = representData(itemValue);

            // If the node value is null (see above) then skip
            if (nodeValue == null) {
                continue;
            }
            
            if (!((nodeKey instanceof ScalarNode && ((ScalarNode) nodeKey).getStyle() == null))) {
                bestStyle = false;
            }
            if (!((nodeValue instanceof ScalarNode && ((ScalarNode) nodeValue).getStyle() == null))) {
                bestStyle = false;
            }
            value.add(new NodeTuple(nodeKey, nodeValue));
        }
        if (flowStyle == null) {
            if (defaultFlowStyle != null) {
                node.setFlowStyle(defaultFlowStyle);
            } else {
                node.setFlowStyle(bestStyle);
            }
        }
        return node;
    }

    @Override
    protected Node representSequence( String tag, List<? extends Object> sequence, Boolean flowStyle )
    {
        // TODO: skipping empty sequences should likely be an option of the dumper (and probably default to true)
        if ( sequence.isEmpty() )
        {
            return null;
        }

        return super.representSequence( tag, sequence, flowStyle );
    }

    private Node representJavaBean( Set<Property> properties, Object javaBean )
    {
        List<NodeTuple> value = new LinkedList<NodeTuple>();
        String tag;
        String customTag = Tags.MAP;
        tag = customTag != null ? customTag : Tags.getGlobalTagForClass( javaBean.getClass() );
        // flow style will be chosen by BaseRepresenter
        MappingNode node = new MappingNode( tag, value, null );
        representedObjects.put( objectToRepresent, node );
        boolean bestStyle = true;
        for ( Property property : properties )
        {
            ScalarNode nodeKey = (ScalarNode) representData( property.getName() );
            Object memberValue = property.get( javaBean );
            boolean hasAlias = false;
            if ( representedObjects.containsKey( memberValue ) )
            {
                // the first occurrence of the node must keep the tag
                hasAlias = true;
            }
            Node nodeValue = representData( memberValue );

            // TODO: The impl seems not to allow to skip certain values
            if ( nodeValue == null )
            {
                continue;
            }

            // if possible try to avoid a global tag with a class name
            if ( nodeValue instanceof MappingNode && !hasAlias )
            {
                // the node is a map, set or JavaBean
                if ( !Map.class.isAssignableFrom( memberValue.getClass() ) )
                {
                    // the node is set or JavaBean
                    if ( property.getType() == memberValue.getClass() )
                    {
                        // we do not need global tag because the property
                        // Class is the same as the runtime class
                        nodeValue.setTag( Tags.MAP );
                    }
                }
            }
            else if ( memberValue != null && Enum.class.isAssignableFrom( memberValue.getClass() ) )
            {
                nodeValue.setTag( Tags.STR );
            }
            if ( nodeKey.getStyle() != null )
            {
                bestStyle = false;
            }
            if ( !( ( nodeValue instanceof ScalarNode && ( (ScalarNode) nodeValue ).getStyle() == null ) ) )
            {
                bestStyle = false;
            }
            value.add( new NodeTuple( nodeKey, nodeValue ) );
        }
        if ( defaultFlowStyle != null )
        {
            node.setFlowStyle( defaultFlowStyle );
        }
        else
        {
            node.setFlowStyle( bestStyle );
        }
        return node;
    }

    private Set<Property> getProperties( Class<? extends Object> type )
        throws IntrospectionException
    {
        Set<Property> properties = new TreeSet<Property>();
        // add JavaBean getters
        for ( PropertyDescriptor property : Introspector.getBeanInfo( type ).getPropertyDescriptors() )
            // TODO: The API lacks an easy way to exclude some properties
            if ( property.getWriteMethod() != null && property.getReadMethod() != null && !property.getReadMethod().getName().equals( "getClass" )
                && !property.getReadMethod().getName().endsWith( "AsMap" )
                && !property.getReadMethod().getName().equals( "getModelEncoding" ) )
            {
                properties.add( new MethodProperty( property ) );
            }
        // add public fields
        for ( Field field : type.getFields() )
        {
            int modifiers = field.getModifiers();
            if ( Modifier.isStatic( modifiers ) || Modifier.isTransient( modifiers ) )
                continue;
            properties.add( new FieldProperty( field ) );
        }
        if ( properties.isEmpty() )
        {
            throw new YAMLException( "No JavaBean properties found in " + type.getName() );
        }
        return properties;
    }

}
