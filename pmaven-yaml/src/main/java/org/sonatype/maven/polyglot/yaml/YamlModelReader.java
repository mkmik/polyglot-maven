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

import org.apache.maven.model.Model;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.polyglot.io.ModelReaderSupport;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

/**
 * YAML model reader.
 *
 * @author jvanzyl
 * @author bentmann
 *
 * @since 0.7
 */
@Component(role = ModelReader.class, hint = "yaml")
public class YamlModelReader
    extends ModelReaderSupport
{
    private final Yaml yaml;

    public YamlModelReader()
    {
        ModelConstructor constructor = new ModelConstructor();
        Loader loader = new Loader( constructor );
        yaml = new Yaml( loader );
    }

    public Model read( Reader input, Map<String, ?> options )
        throws IOException, ModelParseException
    {
        if ( input == null )
        {
            throw new IllegalArgumentException( "YAML Reader is null." );
        }

        return (Model) yaml.load( input );
    }
}
