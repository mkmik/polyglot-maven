package org.sonatype.raven;

import java.util.Map;

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
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tags;

public class ModelConstructor
    extends Constructor
{
    public ModelConstructor()
    {
        super( Model.class );
        this.yamlConstructors.put( Tags.MAP, new ConstructXpp3Dom() );

        TypeDescription modelDescription = new TypeDescription( Model.class );
        addTypeDescription( modelDescription );

        // Parent
        TypeDescription parent = new TypeDescription( Parent.class );
        addTypeDescription( parent );

        // Organization
        TypeDescription organization = new TypeDescription( Organization.class );
        addTypeDescription( organization );

        // Properties   
        //TypeDescription properties = new TypeDescription(Properties.class);
        //properties.setTag( "properties" );
        //constructor.addTypeDescription( properties );
        //modelDescription.putMapPropertyType("properties", String.class, String.class );

        // Developers
        modelDescription.putListPropertyType( "developers", Developer.class );

        // Contributors
        modelDescription.putListPropertyType( "contributors", Contributor.class );

        // DependencyManagement
        TypeDescription dependencyManagement = new TypeDescription( DependencyManagement.class );
        dependencyManagement.putListPropertyType( "dependencies", Dependency.class );
        addTypeDescription( dependencyManagement );

        // Dependencies
        modelDescription.putListPropertyType( "dependencies", Dependency.class );

        // Build
        TypeDescription buildDescription = new TypeDescription( Build.class );
        buildDescription.putListPropertyType( "plugins", Plugin.class );
        addTypeDescription( buildDescription );

        // Modules
        modelDescription.putListPropertyType( "modules", String.class );

        // Profiles
        modelDescription.putListPropertyType( "profiles", Profile.class );

        // Repositories
        modelDescription.putListPropertyType( "repositories", Repository.class );

        // Plugin Repositories
        modelDescription.putListPropertyType( "pluginRepositories", Repository.class );

        // DistributionManagement
        TypeDescription distributionManagement = new TypeDescription( DistributionManagement.class );
        addTypeDescription( distributionManagement );

        // SCM
        TypeDescription scm = new TypeDescription( Scm.class );
        addTypeDescription( scm );

        // IssueManagement
        TypeDescription issueManagement = new TypeDescription( IssueManagement.class );
        addTypeDescription( issueManagement );

        // CiManagement
        TypeDescription ciManagement = new TypeDescription( CiManagement.class );
        addTypeDescription( ciManagement );
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
