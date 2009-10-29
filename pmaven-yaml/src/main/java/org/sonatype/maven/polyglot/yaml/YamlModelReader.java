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
            System.out.println( "HI!");
        }
        
        @Override
        public String resolve( NodeId kind, String value, boolean implicit )
        {
            System.out.println( ">>>>> " + value );
            
            String tag = super.resolve( kind, value, implicit );

            
            return super.resolve( kind, value, implicit );
        }
    }
}
