package org.sonatype.maven.polyglot.yaml;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.model.Build;
import org.apache.maven.model.CiManagement;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Developer;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.IssueManagement;
import org.apache.maven.model.License;
import org.apache.maven.model.Model;
import org.apache.maven.model.ModelBase;
import org.apache.maven.model.Organization;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Profile;
import org.apache.maven.model.Repository;
import org.apache.maven.model.Scm;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tags;
import org.yaml.snakeyaml.resolver.Resolver;

public class ModelConstructor
    extends Constructor
{
    public ModelConstructor()
    {
        super( Model.class );
        this.yamlConstructors.put( Tags.MAP, new ConstructXpp3Dom() );

        TypeDescription modelDescription = new TypeDescription( Model.class );
        addTypeDescription( modelDescription );

        TypeDescription dependencyDescription = new TypeDescription( Dependency.class );
        dependencyDescription.putListPropertyType( "exclusions", Exclusion.class );
        addTypeDescription( dependencyDescription );        
        
        addTypeDescription( new TypeDescription( Parent.class ) );
        addTypeDescription( new TypeDescription( Organization.class ) );

        modelDescription.putListPropertyType( "licenses", License.class );
        modelDescription.putListPropertyType( "developers", Developer.class );
        modelDescription.putListPropertyType( "contributors", Contributor.class );

        TypeDescription dependencyManagement = new TypeDescription( DependencyManagement.class );
        dependencyManagement.putListPropertyType( "dependencies", Dependency.class );
        addTypeDescription( dependencyManagement );

        modelDescription.putListPropertyType( "dependencies", Dependency.class );

        TypeDescription buildDescription = new TypeDescription( Build.class );
        buildDescription.putListPropertyType( "plugins", Plugin.class );
        addTypeDescription( buildDescription );

        TypeDescription pluginDescription = new TypeDescription( Plugin.class );
        pluginDescription.putListPropertyType( "executions", PluginExecution.class );
        addTypeDescription( pluginDescription );        
        
        modelDescription.putListPropertyType( "modules", String.class );
        modelDescription.putListPropertyType( "profiles", Profile.class );
        modelDescription.putListPropertyType( "repositories", Repository.class );
        modelDescription.putListPropertyType( "pluginRepositories", Repository.class );

        addTypeDescription( new TypeDescription( DistributionManagement.class ) );
        addTypeDescription( new TypeDescription( Scm.class ) );
        addTypeDescription( new TypeDescription( IssueManagement.class ) );
        addTypeDescription( new TypeDescription( CiManagement.class ) );
    }

    @Override
    protected Map<Object, Object> constructMapping( MappingNode node )
    {
        Map<Object, Object> mapping = createDefaultMap( node );
        constructMapping2ndStep( node, mapping );
        return mapping;
    }

    // TODO: This should be moved down to SnakeYAML, we shouldn't need to tell how to map Properties
    protected Map<Object, Object> createDefaultMap( Node node )
    {
        if ( node.getType() != null && Properties.class.isAssignableFrom( node.getType() ) )
        {
            return new Properties();
        }
        else
        {
            // respect order from YAML document
            return new LinkedHashMap<Object, Object>();
        }
    }

    private class ConstructXpp3Dom
        implements Construct
    {
        private Xpp3Dom toDom( Map<Object, Object> map )
        {
            Xpp3Dom dom = new Xpp3Dom( "configuration" );

            for ( Map.Entry<Object, Object> entry : map.entrySet() )
            {
                if ( entry.getValue() instanceof Xpp3Dom )
                {
                    Xpp3Dom child = new Xpp3Dom( (Xpp3Dom) entry.getValue(), entry.getKey().toString() );
                    dom.addChild( child );
                }
                else
                {
                    Xpp3Dom child = new Xpp3Dom( entry.getKey().toString() );
                    child.setValue( entry.getValue().toString() );
                    dom.addChild( child );
                }
            }

            return dom;
        }

        public Object construct( Node node )
        {
            return toDom( constructMapping( (MappingNode) node ) );
        }

        public void construct2ndStep( Node node, Object object )
        {
            throw new YAMLException( "Unexpected recursive mapping structure. Node: " + node );
        }
    }
}
