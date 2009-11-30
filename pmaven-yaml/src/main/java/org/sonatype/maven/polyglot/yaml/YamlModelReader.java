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

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.IOUtil;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.resolver.Resolver;
import org.sonatype.maven.polyglot.io.ModelReaderSupport;

/**
 * YAML model reader.
 *
 * @author jvanzyl
 * @author bentmann
 */
@Component(role = ModelReader.class, hint = "yaml")
public class YamlModelReader
    extends ModelReaderSupport
{
    private Yaml yaml;

    public YamlModelReader()
    {
        ModelConstructor constructor = new ModelConstructor();
        Loader loader = new Loader( constructor );              
        loader.setResolver( new ModelResolver() );
        yaml = new Yaml( loader );
    }

    public Model read( Reader input, Map<String, ?> options )
        throws IOException, ModelParseException
    {
        if ( input == null )
        {
            throw new IllegalArgumentException( "YAML Reader is null." );
        }

        Model model = (Model) yaml.load( input );
        IOUtil.close( input );
        return model;
    }

    public class ModelResolver
        extends Resolver
    {
        public ModelResolver()
        {            
            // System.out.println( "HI!");
        }
        
        @Override
        public String resolve( NodeId kind, String value, boolean implicit )
        {
            // System.out.println( ">>>>> " + value );
            
            String tag = super.resolve( kind, value, implicit );

            
            return super.resolve( kind, value, implicit );
        }
    }
}
