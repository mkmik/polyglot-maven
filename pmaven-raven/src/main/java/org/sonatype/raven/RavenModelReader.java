package org.sonatype.raven;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.BufferedInputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.model.Build;
import org.apache.maven.model.CiManagement;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Developer;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.IssueManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Organization;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Profile;
import org.apache.maven.model.Repository;
import org.apache.maven.model.Scm;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.resolver.Resolver;

@Component(role = ModelReader.class, hint = "raven")
public class RavenModelReader
    implements ModelReader
{
    private Yaml yaml;

    public RavenModelReader()
    {
        ModelConstructor constructor = new ModelConstructor();
        Loader loader = new Loader( constructor );              
        loader.setResolver( new ModelResolver() );
        yaml = new Yaml( loader );
    }

    public Model read(final File file, final Map<String,?> options) throws IOException, ModelParseException {
        assert file != null;

        Model model;
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        try {
            model = read(in, options);
            model.setPomFile(file);
        }
        finally {
            IOUtil.close(in);
        }
        return model;
    }

    public Model read( InputStream input, Map<String, ?> options )
        throws IOException, ModelParseException
    {
        return read( new InputStreamReader( input ), options );
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
