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
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.polyglot.io.ModelWriterSupport;
import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tags;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * YAML model writer.
 *
 * @author jvanzyl
 * @author bentmann
 *
 * @since 0.7
 */
@Component(role = ModelWriter.class, hint = "yaml")
public class YamlModelWriter
    extends ModelWriterSupport
{
    public void write( Writer output, Map<String, Object> o, Model model )
        throws IOException
    {
        DumperOptions options = new DumperOptions();
        options.setExplicitRoot( Tags.MAP );
        options.setDefaultFlowStyle( FlowStyle.AUTO );
        options.setIndent( 2 );
        options.setWidth( 80 );

        Representer representer = new ModelRepresenter();

        Dumper dumper = new Dumper( representer, options );
        Yaml yaml = new Yaml( dumper );
        yaml.dump( model, output );
    }

}
